package alrightsolutions.example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import alrightsolutions.example.Model.Music;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by JohnConnor on 28-Sep-16.
 */
public class SearchActivity extends AppCompatActivity {
    Activity context;
    EditText editText;
    RecyclerView sRecyclerview;
    RecyclerView.LayoutManager linearLayoutManager;
    RecyclerView.Adapter adapter;
    RealmConfiguration realmConfig;
    Realm realm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        context=MyApplication.getMainContext();
        editText=(EditText)findViewById(R.id.search);
        sRecyclerview=(RecyclerView)findViewById(R.id.searchlist);
        linearLayoutManager=new LinearLayoutManager(SearchActivity.this);
        sRecyclerview.setLayoutManager(linearLayoutManager);
        realmConfig = new RealmConfiguration.Builder(SearchActivity.this).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfig);
        operation();
    }
    RealmResults<Music> musics;
    void operation()
    {

        Observable<OnTextChangeEvent> phoneText =
                WidgetObservable.text(editText);
        phoneText.filter(new Func1<OnTextChangeEvent, Boolean>() {
            @Override
            public Boolean call(OnTextChangeEvent onTextChangeEvent) {

                return onTextChangeEvent.text().length() > 0;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OnTextChangeEvent>() {
                    @Override
                    public void call(OnTextChangeEvent onTextChangeEvent) {
                        musics=realm.where(Music.class).beginsWith("musicName",onTextChangeEvent.text().toString()).findAll();
                        List<String> musicName=new ArrayList<String>();
                        List<String> musicAdd=new ArrayList<String>();
                        List<String> musicArtist=new ArrayList<String>();
                        List<String> musicImage=new ArrayList<String>();
                        for(int i=0;i<musics.size();i++)
                        {
                            musicName.add(musics.get(i).getMusicName());
                            musicAdd.add(musics.get(i).getMusicURI());
                            musicArtist.add(musics.get(i).getMusicArtist());
                            musicImage.add(musics.get(i).getAlbumArt());
                        }
                        adapter=new SearchAdapter(SearchActivity.this,musicName,musicAdd,musicArtist,musicImage,context);
                        sRecyclerview.setAdapter(adapter);
                    }
                });
    }
}
