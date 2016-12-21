package alrightsolutions.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import alrightsolutions.example.com.devadvance.circularseekbar.CircularSeekBar;
import shortroid.com.shortroid.ShortAnimation.ShortAnimation;

import static alrightsolutions.example.ListViewPopulator.ARTIST;
import static alrightsolutions.example.ListViewPopulator.IMAGE;
import static alrightsolutions.example.ListViewPopulator.NAME;
import static alrightsolutions.example.ListViewPopulator.PROGRESS;
import static alrightsolutions.example.MusicService.mediaPlayer;

/**
 * Created by JohnConnor on 29-Sep-16.
 */

public class MusicActivity extends AppCompatActivity {
    String image,name,artist;
    ImageView albumArt;
    TextView musicName,musicArtist;
    Animation animation;
    FloatingActionButton musicControl;
    CircularSeekBar seekBar;
    int flag=0;
    void operation()
    {

                if (IMAGE != null && IMAGE.length() > 0) {
                    Picasso.with(MusicActivity.this).load(new File(IMAGE)).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            albumArt.setImageBitmap(bitmap);
                            flag=0;
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                } else {
                    albumArt.setImageDrawable(getResources().getDrawable(R.drawable.music));
                }


        musicName.setText(NAME);
        musicArtist.setText(ARTIST);

    }
    ViewGroup transitionLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.change_bound_with_arc);
            transition.setDuration(300);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setSharedElementEnterTransition(transition);
            }
        }

        setContentView(R.layout.music_main);
        transitionLayout=(ViewGroup)findViewById(R.id.transitionLayout);
        albumArt=(ImageView)findViewById(R.id.album_art);
        musicName=(TextView)findViewById(R.id.music_name);
        musicControl=(FloatingActionButton)findViewById(R.id.fab);
        musicArtist=(TextView)findViewById(R.id.music_artist);
        seekBar=(CircularSeekBar) findViewById(R.id.appCompatSeekBar);
        animation= AnimationUtils.loadAnimation(MusicActivity.this,R.anim.rotate_animation);
        animation.setInterpolator(new LinearInterpolator());
        Intent intent=getIntent();
        Bundle b=intent.getBundleExtra("music");
        image=b.getString("image");
        name=b.getString("name");
        artist=b.getString("artist");
        musicName.setText(name);
        musicArtist.setText(artist);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(transitionLayout);
        }
        ShortAnimation.TranslateAnimation translateAnimation=new ShortAnimation.TranslateAnimation(musicName,-1000f,0f,0f,0f);
        translateAnimation.setInterpolator(new FastOutSlowInInterpolator());
        translateAnimation.setDuration(1000);
        translateAnimation.startAnimation();
        ShortAnimation.TranslateAnimation translateAnimation1=new ShortAnimation.TranslateAnimation(musicArtist,-1000f,0f,0f,0f);
        translateAnimation1.setInterpolator(new FastOutSlowInInterpolator());
        translateAnimation1.setDuration(1000);
        translateAnimation1.startAnimation();
        if(image!=null && image.length()>0) {
            Picasso.with(MusicActivity.this).load(new File(image)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    albumArt.setImageBitmap(bitmap);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        else
        {
            albumArt.setImageDrawable(getResources().getDrawable(R.drawable.music));
        }
        if(mediaPlayer.isPlaying())
        {
            albumArt.startAnimation(animation);
            musicControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));

        }
        else
        {
            animation.cancel();
            musicControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
        }
        musicControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    musicControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                    animation.cancel();
                }
                else
                {    albumArt.startAnimation(animation);
                    mediaPlayer.start();
                    musicControl.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                }
            }
        });
        cont_seek();
        seeker();

    }

    @Override
    protected void onPause() {

        try {
            Thread.currentThread().interrupt();
            thread.interrupt();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onDetachedFromWindow() {
        try {
            Thread.currentThread().interrupt();
            thread.interrupt();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDetachedFromWindow();

    }



    @Override
    protected void onDestroy() {

        try {
            Thread.currentThread().interrupt();
            thread.interrupt();
            Log.d("log","destroyed");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    void cont_seek(){
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(PROGRESS);
        Runnable runnable=new Runnable() {

            @Override
            public void run() {
                String time;
               // Log.d(TAG, "run");
                while(mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        PROGRESS = mediaPlayer.getCurrentPosition();
                        int min = (PROGRESS / 1000) / 60;
                        Log.d("auto", "seek");
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
                                if (seekBar.getProgress() >= 0 && seekBar.getProgress() < 1500) {
                                    flag = 1;
                                    operation();
                                    Log.d("log", "operation called");
                                }
                                Log.d("log", mediaPlayer.getDuration() + " " + seekBar.getProgress());
                                seekBar.setMax(mediaPlayer.getDuration());
                                seekBar.setProgress(PROGRESS);
                            }
                        });
                    }
                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ////

                }
            }
        };
        thread=new Thread(runnable);
        thread.start();
    }

    Thread thread;
    void seeker(){
        seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            String time_sec;
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
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
              //      Log.d("blah","blah");
                //    new Runnable() {
                  //      @Override
                    //    public void run() {
                            int min=(PROGRESS/1000)/60;
                            int sec=(PROGRESS/1000)%60;
                            if(sec<10)
                                time_sec="0"+sec;
                            else
                                time_sec=""+sec;
                            String elapsedTime=min+":"+time_sec+"";
                            //    timer.setText(elapsedTime);
                           // mediaPlayer.seekTo(PROGRESS);
                   // circularSeekBar.setProgress(PROGRESS);
                           // new Handler().postDelayed(this,1000);
                      //  }


//                    }.run();
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                if(mediaPlayer!=null){mediaPlayer.seekTo(PROGRESS);}
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
      /*  seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            // int seek_progress;
            String time_sec;
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                PROGRESS=progress;
                //seek_progress=seek_progress*1000;
                new Runnable() {
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

                if(fromUser) {
                    Log.d("blah","blah");
                    new Runnable() {
                        @Override
                        public void run() {
                            int min=(PROGRESS/1000)/60;
                            int sec=(PROGRESS/1000)%60;
                            if(sec<10)
                                time_sec="0"+sec;
                            else
                                time_sec=""+sec;
                            String elapsedTime=min+":"+time_sec+"";
                            //    timer.setText(elapsedTime);
                            //mediaPlayer.seekTo(seek_progress);
                            new Handler().postDelayed(this,1000);
                        }


                    }.run();
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
*/
    }
}
