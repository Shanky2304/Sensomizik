package alrightsolutions.example;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
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
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import alrightsolutions.example.Customizables.OverlapDecoration;
import alrightsolutions.example.Model.Music;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.functions.Func1;
import shortroid.com.shortroid.ShortRoidPreferences.FileNameException;
import shortroid.com.shortroid.ShortRoidPreferences.ShortRoidPreferences;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

import static alrightsolutions.example.ListViewPopulator.NOW_PLAYING;
import static alrightsolutions.example.ListViewPopulator.PROGRESS;
import static alrightsolutions.example.ListViewPopulator.animation;
import static alrightsolutions.example.ListViewPopulator.flag;
import static alrightsolutions.example.MusicService.mediaPlayer;

public class MainActivity extends AppCompatActivity{
    int i=0;
    int MY_PERMISSIONS=1;
    int count = 0,x=0;
    FrameLayout frameLayout;
    FloatingActionButton floatingActionButton;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    List<String> musicName,musicArtist;
    List<String> musicAdd,musicImage;
    List<Integer> musicId;
    RecyclerView.LayoutManager linearLayoutManager;
    ShortRoidPreferences shortRoidPreferences;
    PhoneStateListener phoneStateListener;
    RealmConfiguration realmConfig;
    Realm realm;
    TextView musicNameView,musicArtistView;
    EditText searchEditText;
    AppCompatSeekBar appCompatSeekBar;
    private boolean wasOnCall;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("log","saved preferences");
        shortRoidPreferences.setPrefBoolean("instance",true);
        shortRoidPreferences.setPrefString("musicName",ListViewPopulator.NAME);
        shortRoidPreferences.setPrefString("musicArtist",ListViewPopulator.ARTIST);
        shortRoidPreferences.setPrefString("musicArt",ListViewPopulator.IMAGE);
        shortRoidPreferences.setPrefInt("musicPosition", NOW_PLAYING);
    }
    int songsCount=0;
    void cont_seek(){
        appCompatSeekBar.setMax(mediaPlayer.getDuration());
        appCompatSeekBar.setProgress(PROGRESS);
        Runnable runnable=new Runnable() {

            @Override
            public void run() {
                String time;
                //  Log.d(TAG, "run");
                while(mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        PROGRESS = mediaPlayer.getCurrentPosition();
                        int min = (PROGRESS / 1000) / 60;
                        //        Log.d("auto", "seek");
                        int sec = (PROGRESS / 1000) % 60;
                        if (sec < 10)
                            time = "0" + sec;
                        else
                            time = "" + sec;
                        final String elapsedTime = min + ":" + time + "";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // timer.setText(elapsedTime);
                                if(flag==1)
                                {   flag=0;
                                     Log.d("log","Executed here");
                                    adapter.notifyItemChanged(NOW_PLAYING);
                                    floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                                }
                                appCompatSeekBar.setMax(mediaPlayer.getDuration());
                                appCompatSeekBar.setProgress(PROGRESS);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ////
                    }
                    else
                    {   if(flag==0) {
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // timer.setText(elapsedTime);
                                flag = 1;
                                animation.cancel();
                                floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));

                            }

                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                }
            }
        };
        thread=new Thread(runnable);
        thread.start();
    }
    Thread thread;

    void seeker(){
        appCompatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // int seek_progress;
            String time_sec;
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                PROGRESS=progress;
                //seek_progress=seek_progress*1000;
             /*   new Runnable() {
                    @Override
                    public void run() {
                        int min=(PROGRESS/1000)/60;
                        int sec=(PROGRESS/1000)%60;
                        if(sec<10)
                            time_sec="0"+sec;
                        else
                            time_sec=""+sec;
                        String elapsedTime=min+":"+time_sec+"";
//                        timer.setText(elapsedTime);
                        //mediaPlayer.seekTo(seek_progress);
                        new Handler().postDelayed(this,1000);
                    }
                }.run();
*/
                if(fromUser) {
                    //  Log.d("blah","blah");
                    //       new Runnable() {
                    //         @Override
                    //       public void run() {
                    int min=(PROGRESS/1000)/60;
                    int sec=(PROGRESS/1000)%60;
                    if(sec<10)
                        time_sec="0"+sec;
                    else
                        time_sec=""+sec;
                    String elapsedTime=min+":"+time_sec+"";
                    //    timer.setText(elapsedTime);
                    //  mediaPlayer.seekTo(PROGRESS);
                    //         new Handler().postDelayed(this,1000);
                    //       }


                    //}.run();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer!=null){mediaPlayer.seekTo(PROGRESS);}
            }
        });

    }
    void init()
    {
        musicNameView=(TextView)findViewById(R.id.music_name);
        musicArtistView=(TextView)findViewById(R.id.music_artist);
        appCompatSeekBar=(AppCompatSeekBar)findViewById(R.id.appCompatSeekBar);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        if(shortRoidPreferences.getPrefBoolean("instance"))
        {
            ListViewPopulator.NAME=shortRoidPreferences.getPrefString("musicName");
            ListViewPopulator.ARTIST=shortRoidPreferences.getPrefString("musicArtist");
            ListViewPopulator.IMAGE=shortRoidPreferences.getPrefString("musicArt");
            NOW_PLAYING=shortRoidPreferences.getPrefInt("musicPosition");
            String s=ListViewPopulator.NAME;
            if(s.length()>20)
                s=s.substring(0,20);
            musicNameView.setText(s);
            s=ListViewPopulator.ARTIST;
            if(s.length()>20)
                s=s.substring(0,20);
            musicArtistView.setText(s);
            cont_seek();
            seeker();
        }
    }
    void search()
    {
        searchEditText=(EditText)findViewById(R.id.searchEdittext);
        operation();
    }
    RealmResults<Music> musics;

    void operation()
    {

        Observable<OnTextChangeEvent> phoneText =
                WidgetObservable.text(searchEditText);
        phoneText.filter(new Func1<OnTextChangeEvent, Boolean>() {
            @Override
            public Boolean call(OnTextChangeEvent onTextChangeEvent) {

                return onTextChangeEvent.text().length() >= 0;
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
                        List<Integer> musicId=new ArrayList<Integer>();
                        for(int i=0;i<musics.size();i++)
                        {   musicId.add(musics.get(i).getId());
                            musicName.add(musics.get(i).getMusicName());
                            musicAdd.add(musics.get(i).getMusicURI());
                            musicArtist.add(musics.get(i).getMusicArtist());
                            musicImage.add(musics.get(i).getAlbumArt());
                        }
                        adapter = new ListViewPopulator(MainActivity.this,musicId, musicName, musicAdd, musicArtist, musicImage);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
    Toolbar toolbar;
    FloatingSearchView mSearchView;
    ImageView searchImage;
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isMyServiceRunning(MusicService.class))
        startService(new Intent(this,MusicService.class));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0f);
        MyApplication.setMainActivity(this);
        try {
            shortRoidPreferences=new ShortRoidPreferences(MainActivity.this,"music");
        } catch (FileNameException e) {
            e.printStackTrace();
        }

        init();
        search();
        searchImage=(ImageView)findViewById(R.id.search_image);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterReveal();
            }
        });

        realmConfig = new RealmConfiguration.Builder(MainActivity.this).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfig);



        musicName=new ArrayList<>();
        musicArtist=new ArrayList<>();
        musicAdd=new ArrayList<>();
        musicImage=new ArrayList<>();
        musicId=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.lview);

        phoneStateListener=new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                    if (state == TelephonyManager.CALL_STATE_RINGING) {
                        wasOnCall=true;
                        try{mediaPlayer.pause();}catch (Exception e){e.printStackTrace();}
                    } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                        if(wasOnCall){
                       try{ mediaPlayer.start();}catch (Exception e){e.printStackTrace();}}
                    } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        try {
                            wasOnCall=true;
                            mediaPlayer.pause();
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
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller)findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recyclerView);

        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());

        //recyclerView.setHasFixedSize(true);
        ContentResolver cr = getContentResolver();
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


            } else {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS);


            }
        }
        if(permissionCheck== PackageManager.PERMISSION_GRANTED) {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
            String sortorder = MediaStore.Audio.Media.TITLE;
            final List<Music> musics=new ArrayList<>();
            final int realmCount=getRealmCount();
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
                            int ra=new Random().nextInt();
                            if(b) {
                                final Music music = new Music();
                                music.setId(ra);
                                music.setMusicName(name);
                                music.setMusicURI(data);
                                music.setMusicArtist(artist);
                                music.setAlbumArt(image);
                              //  music.setAlbumId(albumId);

                                musics.add(music);
                            }
                            musicId.add(ra);
                            musicName.add(name);
                            musicAdd.add(data);
                            musicArtist.add(artist);
                            musicImage.add(image);
                            songsCount++;
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
                                    adapter = new ListViewPopulator(MainActivity.this,musicId, musicName, musicAdd, musicArtist, musicImage);
                                    recyclerView.setAdapter(adapter);
                                }
                                if(songsCount!=realmCount)
                                {
                                    updateRealm();
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
    void updateRealm()
    {   realm.executeTransaction(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            realm.delete(Music.class);
        }
    });

        final List<Music> musicList=new ArrayList<>();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(int i=0;i<musicId.size();i++)
                {
                    Music music=new Music();
                    music.setId(musicId.get(i));
                    music.setMusicName(musicName.get(i));
                    music.setMusicArtist(musicArtist.get(i));
                    music.setMusicURI(musicAdd.get(i));
                    music.setAlbumArt(musicImage.get(i));
                    musicList.add(music);
                }
                realm.copyToRealm(musicList);
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
    int getRealmCount()
    {
        RealmResults<Music> musicRealmResults=realm.where(Music.class).findAll();
        Log.d("Mine Realm",musicRealmResults.size()+"");
        return musicRealmResults.size();
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
            adapter = new ListViewPopulator(MainActivity.this,musicId, musicName, musicAdd, musicArtist, musicImage);
            recyclerView.setAdapter(adapter);


    }
    void enterReveal() {
        final View myView = findViewById(R.id.card_reveal);
        final View view1=findViewById(R.id.search_image);

        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim =
                    null;
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            view1.setVisibility(View.GONE);
            myView.setVisibility(View.VISIBLE);
            anim.start();
        }
        else{
            Animation animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_out);
            view1.setVisibility(View.GONE);
            myView.setVisibility(View.VISIBLE);
            myView.startAnimation(animation);
    }

    }

    void exitReveal() {
        // previously visible view
        final View myView = findViewById(R.id.card_reveal);
        final View view1=findViewById(R.id.search_image);
        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth() / 2;

        // create the animation (the final radius is zero)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim =null;
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                    view1.setVisibility(View.VISIBLE);

                }
            });

            // start the animation
            anim.start();
        }
        else
        {
            Animation animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.fade_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    myView.setVisibility(View.INVISIBLE);
                    view1.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            myView.startAnimation(animation);
        }

    }

    @Override
    public void onBackPressed() {
        final View myView = findViewById(R.id.card_reveal);
        if(myView.getVisibility()==View.INVISIBLE)
        super.onBackPressed();
        else {
            if(searchEditText.getText().length()>0)
            searchEditText.setText("");
            exitReveal();
        }
    }
}

