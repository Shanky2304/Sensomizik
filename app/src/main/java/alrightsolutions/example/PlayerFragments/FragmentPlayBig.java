package alrightsolutions.example.PlayerFragments;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import alrightsolutions.example.ListViewPopulator;
import alrightsolutions.example.R;

import static alrightsolutions.example.ListViewPopulator.PROGRESS;
import static alrightsolutions.example.ListViewPopulator.mediaPlayer;

/**
 * Created by JohnConnor on 31-Jul-16.
 */
public class FragmentPlayBig extends Fragment {

    int i=1,k=1,f=1;
    public FragmentPlayBig()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    Button play;
    ImageView musicImageBlur,musicImage;
    TextView musicName,musicArtist;
    LinearLayout linePlay;
    SeekBar seekBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music_player, container, false);
        initialize(rootView);
        setUI();
        setControls();
        cont_seek();
        seeker();
        return rootView;
    }
    void initialize(View view)
    {
        musicImageBlur=(ImageView)view.findViewById(R.id.music_image_blured);
        musicImage=(ImageView)view.findViewById(R.id.music_image_big);
        musicName=(TextView)view.findViewById(R.id.music_name_big);
        musicArtist=(TextView)view.findViewById(R.id.music_artist_big);
        play=(Button)view.findViewById(R.id.music_play_big);
        linePlay=(LinearLayout)view.findViewById(R.id.line_play);
        try {
            seekBar = (SeekBar) view.findViewById(R.id.seekbar);
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(PROGRESS);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void setControls()
    {   if(ListViewPopulator.mediaPlayer.isPlaying())
        play.setBackground(getResources().getDrawable(R.drawable.pause));
        else
        play.setBackground(getResources().getDrawable(R.drawable.play));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==1)
                {
                    ListViewPopulator.mediaPlayer.pause();
                    play.setBackground(getResources().getDrawable(R.drawable.play));
                    i=0;
                }
                else
                {
                    ListViewPopulator.mediaPlayer.start();
                    play.setBackground(getResources().getDrawable(R.drawable.pause));
                    i=1;
                }
            }
        });
        ourThread=new OurThread();
        ourThread.start();
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
    String s="",s1="";
    void setUI()
    {    if(ListViewPopulator.SONG_NAME.length()>20)
    {
        s=ListViewPopulator.SONG_NAME.substring(0,20);
        s=s+"....";
    }
    else
        s=ListViewPopulator.SONG_NAME;
        if(ListViewPopulator.SONG_ARTIST.length()>20)
        {
            s1=ListViewPopulator.SONG_ARTIST.substring(0,20);
            s1=s1+"....";
        }
        else
            s1=ListViewPopulator.SONG_ARTIST;
        musicName.setText(s);
        musicArtist.setText(s1);
        setImages();
    }
    void setImages()
    {   try {
        Picasso.with(getActivity()).load(new File(ListViewPopulator.SONG_IMAGE)).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                /*Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int primary = getResources().getColor(R.color.colorPrimaryDark);
                        int x = palette.getMutedColor(primary);
                        musicImageBlur.setImageResource(R.color.colorText);
                        //   Drawable d=musicImageBlur.getDrawable();
                        // Bitmap myBitmap = ((ColorDrawable)d).
                        // musicImageBlur.setImageBitmap(NativeStackBlur.process(myBitmap,30));
                        musicImage.setImageBitmap(bitmap);
                        linePlay.setBackgroundColor(x);
                    }
                });*/
                Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        int defaults = 0x000000;
                        int primary = palette.getLightMutedColor(defaults);
                  //      int x = palette.getMutedColor(primary);
                        musicImageBlur.setImageBitmap(bitmap);

                           Drawable d=musicImageBlur.getDrawable();
                         Bitmap myBitmap = ((BitmapDrawable)d).getBitmap();
                         musicImageBlur.setImageBitmap(NativeStackBlur.process(myBitmap,150));
                        musicImage.setImageBitmap(bitmap);
                     //   linePlay.setBackgroundColor(primary);
                    }

                };
                if (bitmap != null && !bitmap.isRecycled()) {
                    Palette.from(bitmap).generate(paletteListener);
                }
            }


            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                musicImageBlur.setImageDrawable(errorDrawable);
                musicImage.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }catch (Exception e)
    {
        e.printStackTrace();
    }
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
