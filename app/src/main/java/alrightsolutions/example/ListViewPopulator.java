package alrightsolutions.example;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by 1405089 on 7/15/2016.
 */
public class ListViewPopulator extends RecyclerView.Adapter<ListViewPopulator.ViewHolder>{
    private static final String TAG = "ListViewPopulator";
    List<String> musicName;
    List<String> musicAdd;
    Activity context;
    MediaPlayer mediaPlayer;
    Button media_play;
    Button media_stop;
    SeekBar seekBar;
    TextView timer;

    public ListViewPopulator(Activity context, List<String> musicName, List<String> musicAdd)
    {
        this.context=context;
        this.musicName=musicName;
        this.musicAdd=musicAdd;
        media_play= (Button)context.findViewById(R.id.play);
        media_stop=(Button)context.findViewById(R.id.pause);
        seekBar= (SeekBar) context.findViewById(R.id.seekbar);
        timer=(TextView) context.findViewById(R.id.timer);


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview,parent,false);
        return new ViewHolder(v);
    }
    void something(String s)
    {
        try{mediaPlayer.stop();}catch (NullPointerException e){e.printStackTrace();}
        //To fetch the location of audio files on disk
        mediaPlayer=MediaPlayer.create(context, Uri.fromFile(new File(s)));
        mediaPlayer.start();
        cont_seek();
        //seekBar.setProgress(0);
        //seekBar.setMax(mediaPlayer.getDuration());

    } int temp=0;

    int i=0;
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String s = musicAdd.get(position);
        final String v = musicName.get(position);
        holder.data.setText(v);
        seeker();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = position + 1;
                something(s);
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
                // mediaPlayer.reset();
                change();
                op();
            }
        });


        media_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.start();
                    Log.d("click", "Kuch bhi");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        media_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer.pause();
                    Log.d("click2", "Kuch bhi2");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

}

    void cont_seek(){
        Runnable runnable=new Runnable() {

            @Override
            public void run() {
                String time;
                Log.d(TAG, "run");
                while(mediaPlayer != null && mediaPlayer.isPlaying()){
                    final int progress = mediaPlayer.getCurrentPosition();
                    int min = (progress / 1000) / 60;
                    Log.d("auto","seek");
                    int sec = (progress / 1000) % 60;
                    if (sec < 10)
                        time = "0" + sec;
                    else
                        time = "" + sec;
                    final String elapsedTime = min + ":" + time + "";
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timer.setText(elapsedTime);
                            seekBar.setMax(mediaPlayer.getDuration());
                            seekBar.setProgress(progress);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }


    void seeker(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seek_progress;
            String time_sec;
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                seek_progress=progress;
                //seek_progress=seek_progress*1000;
                new Runnable() {
                    @Override
                    public void run() {
                        int min=(seek_progress/1000)/60;
                        int sec=(seek_progress/1000)%60;
                        if(sec<10)
                            time_sec="0"+sec;
                        else
                            time_sec=""+sec;
                        String elapsedTime=min+":"+time_sec+"";
                        timer.setText(elapsedTime);
                        //mediaPlayer.seekTo(seek_progress);
                        new Handler().postDelayed(this,1000);
                    }
                }.run();

                if(fromUser) {
                        Log.d("blah","blah");
                    new Runnable() {
                        @Override
                        public void run() {
                            int min=(seek_progress/1000)/60;
                            int sec=(seek_progress/1000)%60;
                            if(sec<10)
                                time_sec="0"+sec;
                            else
                                time_sec=""+sec;
                            String elapsedTime=min+":"+time_sec+"";
                            timer.setText(elapsedTime);
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
                    if(mediaPlayer!=null){mediaPlayer.seekTo(seek_progress);}
            }
        });

    }
    int h=0;
    void change(){
        try{mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

            if(temp!=musicAdd.size())
            something(musicAdd.get(temp));
            temp=temp+1;
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            change();
        }


    });


        }catch(Throwable throwable){throwable.printStackTrace();}}
    void op()
{
    Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
        @Override
        public void onFaceUp() {
            if (i == 0) {
                try {
                    mediaPlayer.start();
                    i = 1;
                    h=1;
                }catch (NullPointerException e){e.printStackTrace();}
            }
        }

        @Override
        public void onFaceDown() {
            if (i == 1) {
                try {
                    mediaPlayer.pause();
                    i = 0;
                    h=0;
                }catch (NullPointerException e){e.printStackTrace();}
            }
        }
    });
    
    Sensey.getInstance().startShakeDetection(15, new ShakeDetector.ShakeListener() {
        @Override
        public void onShakeDetected() {
            try {
                if(h==1)
                something(musicAdd.get(randomG(musicAdd.size()-1,0)));
            }catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    });
}
    int randomG(int max,int min)
    {
        Random random=new Random();
        return random.nextInt((max-min)+1);
    }
    @Override
    public int getItemCount() {
        return musicName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
   {
        TextView data;
       public ViewHolder(View itemView) {
           super(itemView);
           data=(TextView)itemView.findViewById(R.id.data);
       }
   }
}
