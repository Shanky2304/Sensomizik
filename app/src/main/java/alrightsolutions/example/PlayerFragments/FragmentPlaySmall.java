package alrightsolutions.example.PlayerFragments;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import alrightsolutions.example.ListViewPopulator;
import alrightsolutions.example.R;

import static alrightsolutions.example.ListViewPopulator.PROGRESS;
import static alrightsolutions.example.ListViewPopulator.mediaPlayer;

/**
 * Created by JohnConnor on 30-Jul-16.
 */
public class FragmentPlaySmall extends Fragment {
    public static int FragmentLength=40;
    public FragmentPlaySmall()
    {
        //Required Empty Constructor
    }
    MediaPlayer.TrackInfo trackInfo[];
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    TextView title,album;
    Button play;
    RelativeLayout relativeLayout;
    SeekBar seekBar;
    String s,s1;
    ImageView musicImage;

    int i=1,k=1,f=1;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_play, container, false);
        title=(TextView)rootView.findViewById(R.id.music_name);
        album=(TextView)rootView.findViewById(R.id.music_artist);
        play=(Button)rootView.findViewById(R.id.music_play);
        seekBar=(SeekBar)rootView.findViewById(R.id.seekbar);

        relativeLayout=(RelativeLayout)rootView.findViewById(R.id.rel_play);
       // slidingUpPanelLayout=(SlidingUpPanelLayout)getActivity().findViewById(R.id.sliding_layout);
     /*   relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(),"Clicked dd",Toast.LENGTH_LONG).show();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });*/
        musicImage=(ImageView)rootView.findViewById(R.id.music_art);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(PROGRESS);
        ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                relativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                FragmentLength = relativeLayout.getMeasuredHeight();
              //  Toast.makeText(getActivity().getApplicationContext(),""+FragmentLength,Toast.LENGTH_LONG).show();
            }
        });

        if(ListViewPopulator.SONG_NAME.length()>25)
        {
            s=ListViewPopulator.SONG_NAME.substring(0,25);
            s=s+"....";
        }
        else
        s=ListViewPopulator.SONG_NAME;
        if(ListViewPopulator.SONG_ARTIST.length()>25)
        {
            s1=ListViewPopulator.SONG_ARTIST.substring(0,25);
            s1=s1+"....";
        }
        else
        s1=ListViewPopulator.SONG_ARTIST;
        title.setText(s);
        album.setText(s1);
        if(ListViewPopulator.SONG_IMAGE!=null)
        setMusicImage(musicImage,ListViewPopulator.SONG_IMAGE);
        if(mediaPlayer.isPlaying())
            play.setBackground(getResources().getDrawable(R.drawable.pause));
        else
            play.setBackground(getResources().getDrawable(R.drawable.play));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==1)
                {
                    mediaPlayer.pause();
                    play.setBackground(getResources().getDrawable(R.drawable.play));
                    i=0;
                }
                else
                {
                    mediaPlayer.start();
                    play.setBackground(getResources().getDrawable(R.drawable.pause));
                    i=1;
                }
            }
        });
         ourThread=new OurThread();
        ourThread.start();
        seeker();
        cont_seek();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ourThread.interrupt();
//        ourThread.stop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ourThread.interrupt();
      //  ourThread.stop();
    }

    OurThread ourThread;
    class OurThread extends Thread
    {
        @Override
        public void run() {
            super.run();
            while (k>0)
            {
                try {
                    Thread.sleep(800);
                    if(mediaPlayer.isPlaying())
                    {   if(f==1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                play.setBackgroundResource(R.drawable.pause);
                                f = 0;
                            }
                        });
                    }
                    }
                    else
                    {   if(f==0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                play.setBackgroundResource(R.drawable.play);
                                f = 1;
                            }
                        });
                    }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(k==1000)
                    k=2;
                k++;
            }
        }
    }
    void setMusicImage(final ImageView image, String s)
    {
        Picasso.with(getActivity()).load(new File(s)).into(image, new Callback() {
            @Override
            public void onSuccess() {
                Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        int defaults = 0x0;
                        Palette.Swatch swatch = palette.getMutedSwatch();
                        int primary= defaults;
                        int text=defaults;
                        if (swatch != null) {
                            primary = swatch.getRgb();
                            text=swatch.getBodyTextColor();
                        }


                        //      int x = palette.getMutedColor(primary);
                      //  musicImageBlur.setImageResource(R.color.colorText);
                        //   Drawable d=musicImageBlur.getDrawable();
                        // Bitmap myBitmap = ((ColorDrawable)d).
                        // musicImageBlur.setImageBitmap(NativeStackBlur.process(myBitmap,30));
                        //musicImage.setImageBitmap(bitmap);
                     //   relativeLayout.setBackgroundColor(primary);
                       // title.setTextColor(text);
                     //   album.setTextColor(text);
                    }

                };
                Drawable d=image.getDrawable();
                 Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    Palette.from(bitmap).generate(paletteListener);
                }
            }

            @Override
            public void onError() {
                image.setImageResource(R.color.colorPrimaryDark);
            }
        });
    }
    void cont_seek(){

        Runnable runnable=new Runnable() {

            @Override
            public void run() {
                String time;

                Log.d("seek", "run");
                while(mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()){
                        PROGRESS = mediaPlayer.getCurrentPosition();
                    int min = (PROGRESS / 1000) / 60;
                    Log.d("auto", "seek");
                    int sec = (PROGRESS / 1000) % 60;
                    if (sec < 10)
                        time = "0" + sec;
                    else
                        time = "" + sec;
                    final String elapsedTime = min + ":" + time + "";
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // timer.setText(elapsedTime);
                                seekBar.setMax(mediaPlayer.getDuration());
                                seekBar.setProgress(PROGRESS);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ////
                }
                }
            }
        };
        new Thread(runnable).start();
    }


    void seeker(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                       // timer.setText(elapsedTime);
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
                           // timer.setText(elapsedTime);
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

    }
}
