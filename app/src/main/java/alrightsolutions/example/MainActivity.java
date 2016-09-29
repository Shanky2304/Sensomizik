package alrightsolutions.example;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import alrightsolutions.example.Customizables.OverlapDecoration;
import alrightsolutions.example.Model.Music;
import alrightsolutions.example.PlayerFragments.FragmentPlayBig;
import alrightsolutions.example.PlayerFragments.FragmentPlaySmall;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class MainActivity extends FragmentActivity {
    int i=0;
    int MY_PERMISSIONS=1;
    int count = 0,x=0;
    FrameLayout frameLayout;
    FloatingActionButton floatingActionButton;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<String> musicName,musicArtist;
    List<String> musicAdd,musicImage;
    RecyclerView.LayoutManager linearLayoutManager;

    PhoneStateListener phoneStateListener;
    RealmConfiguration realmConfig;
    Realm realm;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    //    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        MyApplication.setMainActivity(this);

        realmConfig = new RealmConfiguration.Builder(MainActivity.this).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfig);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
        musicName=new ArrayList<>();
        musicArtist=new ArrayList<>();
        musicAdd=new ArrayList<>();
        musicImage=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.lview);

        phoneStateListener=new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                    if (state == TelephonyManager.CALL_STATE_RINGING) {
                        //Incoming call: Pause music
                        try{ListViewPopulator.mediaPlayer.pause();}catch (Exception e){e.printStackTrace();}
                    } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                        //Not in call: Play music
                       try{ ListViewPopulator.mediaPlayer.start();}catch (Exception e){e.printStackTrace();}
                    } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        //A call is dialing, active or on hold
                        try {
                            ListViewPopulator.mediaPlayer.pause();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    super.onCallStateChanged(state, incomingNumber);

            }
        };
        linearLayoutManager=new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new OverlapDecoration());
       // VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller)findViewById(R.id.fast_scroller);
       // fastScroller.setRecyclerView(recyclerView);
       // recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

        //recyclerView.setHasFixedSize(true);
        ContentResolver cr = getContentResolver();
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if(permissionCheck== PackageManager.PERMISSION_GRANTED) {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
            String sortorder = MediaStore.Audio.Media.TITLE;
            final List<Music> musics=new ArrayList<>();
            final boolean b=checkEmptyRealm();
            if(!b)
                getFromRealm();
            final Cursor cursor = cr.query(uri, null, selection, null, sortorder);

            //ArrayList<String> arrayList_Title = null;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (cursor != null) {
                        count = cursor.getCount();
                        cursor.moveToFirst();
                        while(count > 0) {
                            String name= cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                            String albumId=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                            Cursor cursor1 = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                    new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                                    MediaStore.Audio.Albums._ID + "=?",
                                    new String[]{String.valueOf(albumId)},
                                    null);
                            String image="";
                            if (cursor1.moveToFirst()) {
                                image = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                                // do whatever you need to do
                            }
                            cursor1.close();
                            //arrayList_Title.add(data);
                            // Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();
                            Log.d("lsf", data);
                            if(b) {
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
                                if(b) {
                                    addToRealm(musics);
                                    Log.d("Mine",musics.size()+"");
                                    adapter = new ListViewPopulator(MainActivity.this, musicName, musicAdd, musicArtist, musicImage);
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

        Sensey.getInstance().init(this);




    }

    @Override
    protected void onResume() {
        super.onResume();
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
            TelephonyManager mngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if(mngr != null) {
                mngr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MY_PERMISSIONS){
            Log.d("okY","okay");
        }
    }
    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.commitAllowingStateLoss();
    }

    void addToRealm(final List<Music> musics)
    {
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
                Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    boolean checkEmptyRealm()
    {
        RealmResults<Music> musicRealmResults=realm.where(Music.class).findAll();
        Log.d("Mine Realm",musicRealmResults.size()+"");
        return musicRealmResults.size() == 0;
    }
    void getFromRealm()
    {
        RealmResults<Music> musicRealmResults=realm.where(Music.class).findAll();
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
            adapter = new ListViewPopulator(MainActivity.this, musicName, musicAdd, musicArtist, musicImage);
            recyclerView.setAdapter(adapter);


    }

}

