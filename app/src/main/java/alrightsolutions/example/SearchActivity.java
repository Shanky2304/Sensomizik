package alrightsolutions.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;



import java.util.ArrayList;
import java.util.List;

import alrightsolutions.example.Model.Music;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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
    ImageView images;
    Realm realm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        context=MyApplication.getMainContext();
        editText=(EditText)findViewById(R.id.search);
        sRecyclerview=(RecyclerView)findViewById(R.id.searchlist);
        images=(ImageView)findViewById(R.id.music_image_blured);
        linearLayoutManager=new LinearLayoutManager(SearchActivity.this);
        sRecyclerview.setLayoutManager(linearLayoutManager);
        realmConfig = new RealmConfiguration.Builder(SearchActivity.this).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfig);
        setBackground();
        operation();
    }
    RealmResults<Music> musics;
    void setBackground()
    {   int primary=ListViewPopulator.BACKGROUND_COLOR;
        final GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[] {primary,Color.BLACK});
        gd.setCornerRadius(0f);
        // gd.setDither(true);
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        final TransitionDrawable transitionDrawable =
                new TransitionDrawable(new Drawable[] { images.getDrawable(), gd });

        images.setImageDrawable(transitionDrawable);
        transitionDrawable.setCrossFadeEnabled(false);
        transitionDrawable.startTransition(2500);
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmaps = Bitmap.createBitmap(images.getWidth(),images.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmaps);
                gd.draw(canvas);
                images.setImageBitmap(NativeStackBlur.process(bitmaps,200));
            }
        },2500);*/


    }
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
                        musics=realm.where(Music.class).contains("musicName",onTextChangeEvent.text().toString(), Case.INSENSITIVE).findAll();
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
