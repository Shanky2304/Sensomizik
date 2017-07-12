package alrightsolutions.example;

//import android.*;
//import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.github.nisrulz.sensey.Sensey;

import java.util.ArrayList;
import java.util.List;

import alrightsolutions.example.Customizables.OverlapDecoration;
import alrightsolutions.example.Model.Music;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Shanky23 on 10/12/2016.
 */

public class SplashActivity extends AppCompatActivity {

    //int i = 0;
    int count = 0;
    static int MY_PERMISSIONS = 1;
    RealmConfiguration realmConfig;
    Realm realm;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<String> musicName, musicArtist;
    List<String> musicAdd, musicImage;
    List<Integer> musicId;
    int permissionCheck = 0;
    ContentResolver cr;
    boolean Permission_Granted;
    //RecyclerView.LayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_content);

        //linearLayoutManager = new LinearLayoutManager(SplashActivity.this);
        //recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(new OverlapDecoration());

        //Intent intent = new Intent(this,MainActivity.class);
        //startActivity(intent);
        //finish();

        musicName = new ArrayList<>();
        musicArtist = new ArrayList<>();
        musicAdd = new ArrayList<>();
        musicId = new ArrayList<>();
        musicImage = new ArrayList<>();
        //recyclerView = (RecyclerView) findViewById(R.id.lview);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionCheck = ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS);
                }

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        }
        if(permissionCheck==PackageManager.PERMISSION_GRANTED){
            Permission_Granted=true;
        }
        new PrefetchFiles().execute();
    }

    private class PrefetchFiles extends AsyncTask<Void, Void, Void> {

        final List<Music> musics = new ArrayList<>();

        @Override
        protected void onPreExecute(){
            Realm.init(SplashActivity.this);
            realm = Realm.getDefaultInstance();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (Permission_Granted) {
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
                String sortorder = MediaStore.Audio.Media.TITLE;

                final boolean b = checkEmptyRealm();
                if (!b) {
                    getFromRealm();
                }
                cr=getContentResolver();
                final Cursor cursor = cr.query(uri, null, selection, null, sortorder);

                //ArrayList<String> arrayList_Title = null;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (cursor != null) {
                            count = cursor.getCount();
                            cursor.moveToFirst();
                            while (count > 0) {
                                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                                Cursor cursor1 = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                        new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                                        MediaStore.Audio.Albums._ID + "=?",
                                        new String[]{String.valueOf(albumId)},
                                        null);
                                String image = "";
                                try {
                                    if (cursor1 != null && cursor1.moveToFirst()) {
                                        image = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                                        // do whatever you need to do
                                    }
                                    if (cursor1 != null) {
                                        cursor1.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //arrayList_Title.add(data);
                                // Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();
                                Log.d("lsf", data);
                                if (b) {
                                    final Music music = new Music();
                                    music.setMusicName(name);
                                    music.setMusicURI(data);
                                    music.setMusicArtist(artist);
                                    music.setAlbumArt(image);
                                    music.setAlbumId(albumId);

                                    musics.add(music);
                                }
                                musicName.add(name);
                                musicAdd.add(data);
                                musicArtist.add(artist);
                                musicImage.add(image);
                                cursor.moveToNext();
                                count--;
                            }
                            cursor.moveToFirst();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (b) {
                                        addToRealm(musics);
                                        Log.d("Mine", musics.size() + "");
                                        adapter = new ListViewPopulator(SplashActivity.this, musicId, musicName, musicAdd, musicArtist, musicImage);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            });

                        }
                        try {
                            if (cursor != null) {
                                cursor.close();
                            }
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }
                }).start();

            }

            Sensey.getInstance().init(getApplicationContext());

            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            super.onPostExecute(null);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

            void addToRealm(final List<Music> musics) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.copyToRealm(musics);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            boolean checkEmptyRealm() {
                RealmResults<Music> musicRealmResults = realm.where(Music.class).findAll();
                Log.d("Mine Realm", musicRealmResults.size() + "");
                return musicRealmResults.size() == 0;
            }

            void getFromRealm() {
                RealmResults<Music> musicRealmResults = realm.where(Music.class).findAll();
                List<String> musicName = new ArrayList<>();
                List<String> musicAdd = new ArrayList<>();
                List<String> musicArtist = new ArrayList<>();
                List<String> musicImage = new ArrayList<>();
                for (int i = 0; i < musicRealmResults.size(); i++) {
                    musicName.add(musicRealmResults.get(i).getMusicName());
                    musicAdd.add(musicRealmResults.get(i).getMusicURI());
                    musicArtist.add(musicRealmResults.get(i).getMusicArtist());
                    musicImage.add(musicRealmResults.get(i).getAlbumArt());
                }
                adapter = new ListViewPopulator(SplashActivity.this, musicId, musicName, musicAdd, musicArtist, musicImage);
                recyclerView.setAdapter(adapter);

            }



    }
}

