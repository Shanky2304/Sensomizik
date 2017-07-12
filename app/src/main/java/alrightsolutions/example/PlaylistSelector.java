package alrightsolutions.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import alrightsolutions.example.Customizables.OverlapDecoration;
import alrightsolutions.example.Model.Music;
import alrightsolutions.example.Model.Playlist;
import alrightsolutions.example.adapter.PlaylistAdapter;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Shanky23 on 2/18/2017.
 */

public class PlaylistSelector extends AppCompatActivity {
   RecyclerView recyclerView;
    RealmConfiguration realmConfig;
    Realm realm;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager linearLayoutManager;
    String playlistname;
    PlaylistAdapter playlistAdapter;
    private RealmResults<Playlist> playlists;
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
       setContentView(R.layout.playlist_selector);
       playlistname = getIntent().getStringExtra("playlistname");
       Realm.init(this);
       realm = Realm.getDefaultInstance();
       recyclerView = (RecyclerView) findViewById(R.id.selectlist);
       linearLayoutManager=new LinearLayoutManager(PlaylistSelector.this);
       recyclerView.setLayoutManager(linearLayoutManager);
       recyclerView.addItemDecoration(new OverlapDecoration());
       getFromRealm();

   }

    void getFromRealm()
    {
        RealmResults<Music> musicRealmResults=realm.where(Music.class).findAll();
        List<Integer> musicId=new ArrayList<>();
        List<String> musicName = new ArrayList<>();
        List<String> musicAdd = new ArrayList<>();
        List<String> musicArtist = new ArrayList<>();
        List<String> musicImage = new ArrayList<>();
        for (int i = 0; i < musicRealmResults.size(); i++) {
            musicId.add(musicRealmResults.get(i).getId());
            musicName.add(musicRealmResults.get(i).getMusicName());
            musicAdd.add(musicRealmResults.get(i).getMusicURI());
            musicArtist.add(musicRealmResults.get(i).getMusicArtist());
            musicImage.add(musicRealmResults.get(i).getAlbumArt());
        }
        adapter = new PlaylistAdapter(PlaylistSelector.this,musicId, musicName, musicAdd, musicArtist, musicImage, playlistname);
        recyclerView.setAdapter(adapter);
        //linearLayoutManager.scrollToPosition(NOW_PLAYING);


    }
    void addToRealm(final List<Playlist> playlists)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.copyToRealm(playlists);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                Toast.makeText(PlaylistSelector.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {

            playlists.add(playlistAdapter.playlist);
            addToRealm(playlists);
        }
        catch (NullPointerException ne){
            ne.printStackTrace();
        }
        super.onBackPressed();
    }
}
