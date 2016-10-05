package alrightsolutions.example;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.FaceDetector;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.commit451.nativestackblur.NativeStackBlur;
import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import alrightsolutions.example.PlayerFragments.FragmentPlayBig;
import alrightsolutions.example.PlayerFragments.FragmentPlaySmall;
import shortroid.com.shortroid.ShortAnimation.ShortAnimation;

/**
 * Created by 1405089 on 7/15/2016.
 */
public class ListViewPopulator extends RecyclerView.Adapter<ListViewPopulator.ViewHolder>{
    private static final String TAG = "ListViewPopulator";
    List<String> musicName,musicImage;
    List<String> musicAdd,musicArtist;
    List<Integer> musicId;
    Activity context;
    TextView musicNameView,musicArtistView;
    FloatingActionButton musicControl;
    public static MediaPlayer mediaPlayer;
    public static int PROGRESS=0;
    AppCompatSeekBar seekBar;
    LinearLayout linearLayout;
    TextView timer;
    public static int BACKGROUND_COLOR=0x00;
    public  static String SONG_NAME="";
    public  static String SONG_ARTIST="";
    public  static String SONG_IMAGE="";
    public static String SONG_ADDRESS="";
    Animation animation;
    DisplayMetrics metrics;
    ImageView previous,next;

    public ListViewPopulator(Activity context,List<Integer> musicId, List<String> musicName, List<String> musicAdd,List<String> musicArtist,List<String> musicImage)
    {
        this.context=context;
        this.musicId=musicId;
        this.musicName=musicName;
        this.musicAdd=musicAdd;
        this.musicArtist=musicArtist;
        this.musicImage=musicImage;
        musicNameView=(TextView)context.findViewById(R.id.music_name);
        musicArtistView=(TextView)context.findViewById(R.id.music_artist);
        musicControl=(FloatingActionButton)context.findViewById(R.id.fab);
        linearLayout=(LinearLayout)context.findViewById(R.id.linears);
        seekBar= (AppCompatSeekBar) context.findViewById(R.id.appCompatSeekBar);
        timer=(TextView) context.findViewById(R.id.timer);
        previous=(ImageView)context.findViewById(R.id.previous);
        next=(ImageView)context.findViewById(R.id.next);
        metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //images=(ImageView)context.findViewById(R.id.music_image_blured);
        animation= AnimationUtils.loadAnimation(context,R.anim.rotate_animation);
        animation.setInterpolator(new LinearInterpolator());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview,parent,false);

        return new ViewHolder(v);
    }

    void something(String s,int h)
    {
        try{mediaPlayer.stop();mediaPlayer.reset();}catch (Exception e){e.printStackTrace();}
        //To fetch the location of audio files on disk
        mediaPlayer=MediaPlayer.create(context, Uri.fromFile(new File(s)));
        mediaPlayer.start();
        try {

            SONG_NAME = musicName.get(temp);
            SONG_ARTIST = musicArtist.get(temp);
            SONG_IMAGE = musicImage.get(temp);
            SONG_ADDRESS = musicAdd.get(temp);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        NAME=musicName.get(h);
        IMAGE=musicImage.get(h);
        ARTIST=musicArtist.get(h);
        notifyItemChanged(NOW_PLAYING);
        NOW_PLAYING=h;
        ID=musicId.get(NOW_PLAYING);
        notifyItemChanged(NOW_PLAYING);
        String s1=musicName.get(h);
        if(s1.length()>20) {
            s1 = s1.substring(0, 20);
            s1=s1+"..";
        }
        String s2=musicArtist.get(h);
        if(s2.length()>20) {
            s2 = s2.substring(0, 20);
            s2=s2+"..";
        }
        musicArtistView.setText(s2);
        musicNameView.setText(s1);
        musicControl.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_white_24dp));
        try {
            //Thread.currentThread().destroy();
           Thread.currentThread().interrupt();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        cont_seek();
        //seekBar.setProgress(0);
        //seekBar.setMax(mediaPlayer.getDuration());

    }

    int temp=0;

    int i=0;
   /* void setImages(String s)
    {   try {
        Picasso.with(context).load(new File(s)).into(new Target() {
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
               /* Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        int defaults = 0x000;
                        int primary = palette.getVibrantColor(defaults);
                        if(primary==defaults)
                        {
                            primary=0xFF304FFE;
                        }
                        BACKGROUND_COLOR=primary;
                        //      int x = palette.getMutedColor(primary);
                        //images.setImageBitmap(bitmap);

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
                        transitionDrawable.startTransition(3000);
                       // Drawable d=images.getDrawable();
                        new Handler().postDelayed(new Runnable() {
                            @Override public void run() {
                                Bitmap bitmaps = Bitmap.createBitmap(images.getWidth(),images.getHeight(), Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmaps);
                                gd.draw(canvas);
                                images.setImageBitmap(NativeStackBlur.process(bitmaps,200));
                            }
                        }, 3000);

                       // ShortAnimation.FadeOutAnimation fadeOutAnimation=new ShortAnimation.FadeOutAnimation(images);
                       // fadeOutAnimation.startAnimation();
                        //images.setImageBitmap(bitmap);
                        //   linePlay.setBackgroundColor(primary);



                    }

                };
                if (bitmap != null && !bitmap.isRecycled()) {
                    Palette.from(bitmap).generate(paletteListener);
                }
            }


            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                images.setImageDrawable(errorDrawable);
                images.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }catch (Exception e)
    {
        e.printStackTrace();
    }
    }*/
    public static int NOW_PLAYING=-1;
    public static String NAME="";
    public static String IMAGE="";
    public static String ARTIST="";
    public static int ID=-1;
    public static int MUSIC_POSITION=0;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String s = musicAdd.get(position);
        final String v = musicName.get(position);
        final String artist=musicArtist.get(position);
        final String image=musicImage.get(position);
        int id=musicId.get(position);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a=NOW_PLAYING;

                NOW_PLAYING=NOW_PLAYING-1;
                ID=musicId.get(NOW_PLAYING);
                temp = NOW_PLAYING;
                notifyItemChanged(a);
                SONG_NAME=v;
                SONG_ARTIST=artist;
                SONG_IMAGE=image;
                something(musicAdd.get(NOW_PLAYING),NOW_PLAYING);

                animation.cancel();
                //holder.albumArt.startAnimation(animation);
                notifyItemChanged(NOW_PLAYING-1);

                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
                // mediaPlayer.reset();
                change();
                // op();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a=NOW_PLAYING;
                NOW_PLAYING=NOW_PLAYING+1;
                ID=musicId.get(NOW_PLAYING);
                notifyItemChanged(a);

                temp = NOW_PLAYING + 1;
                SONG_NAME=v;
                SONG_ARTIST=artist;
                SONG_IMAGE=image;
                something(musicAdd.get(NOW_PLAYING),NOW_PLAYING);

                animation.cancel();
                //holder.albumArt.startAnimation(animation);
                notifyItemChanged(NOW_PLAYING);

                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
                // mediaPlayer.reset();
                change();
            }
        });
        holder.albumArt.setImageDrawable(context.getResources().getDrawable(R.drawable.music));
        if(image!=null&&image.length()>0) {
            Picasso.with(context).load(new File(image)).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    holder.albumArt.setImageBitmap(bitmap);
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
        holder.albumArt.setImageDrawable(context.getResources().getDrawable(R.drawable.music));
        String s1=v;
        if(s1.length()>20) {
            s1 = s1.substring(0, 20);
            s1=s1+"..";
        }
        String s2=artist;
        if(s2.length()>20) {
            s2 = s2.substring(0, 20);
            s2=s2+"..";
        }
                holder.data.setText(s1);
        holder.data_album.setText(s2);
        if(ID==id)
        {       try {

           /* if(mediaPlayer==null)
            {
                mediaPlayer=MediaPlayer.create(context,Uri.fromFile(new File(musicAdd.get(NOW_PLAYING))));
                mediaPlayer.seekTo(MUSIC_POSITION);
                mediaPlayer.pause();
            }*/
            if (mediaPlayer.isPlaying()) {
                holder.albumArt.startAnimation(animation);
                musicControl.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_white_24dp));
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        }
        seeker();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MusicActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("image",IMAGE);
                bundle.putString("name", NAME);
                bundle.putString("artist",ARTIST);
                intent.putExtra("music",bundle);
                Pair<View, String> p1 = Pair.create((View)holder.albumArt, "albumArt");
                Pair<View, String> p2 = Pair.create((View)musicControl, "playButton");
                Pair<View, String> p3 = Pair.create((View)seekBar, "seekbar");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(context, p1,p2,p3);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(intent, options.toBundle());
                }
                else {
                    context.startActivity(intent);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vo) {
                int a=NOW_PLAYING;
                NOW_PLAYING=position;
                ID=musicId.get(NOW_PLAYING);
                notifyItemChanged(a);

                temp = position + 1;
               // if(NOW_PLAYING!=position)
                something(s,position);
                SONG_NAME=v;
                SONG_ARTIST=artist;
                SONG_IMAGE=image;
                animation.cancel();
                holder.albumArt.startAnimation(animation);
                try {
                    seekBar.setProgress(0);
                    seekBar.setMax(mediaPlayer.getDuration());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                // mediaPlayer.reset();
                change();
                op();

            }
        });


        musicControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        musicControl.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));
                        animation.cancel();
                    }
                    else
                    {
                        mediaPlayer.start();
                        musicControl.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                        notifyItemChanged(NOW_PLAYING);
                    }
                //    Log.d("click", "Kuch bhi");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


}
    static int flag=0;
    void cont_seek(){
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(PROGRESS);
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

                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // timer.setText(elapsedTime);
                                if(flag==1)
                                {   flag=0;
                                   // Log.d("log","Executed here");
                                    notifyItemChanged(NOW_PLAYING);
                                    musicControl.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                                }
                                seekBar.setMax(mediaPlayer.getDuration());
                                seekBar.setProgress(PROGRESS);
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
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // timer.setText(elapsedTime);
                                flag = 1;
                                animation.cancel();
                                musicControl.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp));

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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
       int h=0;
    void change(){
        try{mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {

            if(temp!=musicAdd.size()-1)
            something(musicAdd.get(temp),temp);
            Log.d("log","ChangeCalled");
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
                    notifyItemChanged(NOW_PLAYING);
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
                    notifyItemChanged(NOW_PLAYING);
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
                if(h==1) {
                    temp=randomG(musicAdd.size() - 1, 0);
                    something(musicAdd.get(temp),temp);
                }
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
        TextView data,data_album;
       ImageView albumArt;
       public ViewHolder(View itemView) {
           super(itemView);
           data=(TextView)itemView.findViewById(R.id.data);
           data_album=(TextView)itemView.findViewById(R.id.data_album);
           albumArt=(ImageView)itemView.findViewById(R.id.album_art);
       }
   }
  /*  public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);

        }

    }
    private void fragmentJump(int flag) {
        if(flag==0) {
            FragmentPlaySmall fragmentPlaySmall = new FragmentPlaySmall();
            switchContent(R.id.frame, fragmentPlaySmall);
        }
        else {
            FragmentPlayBig fragmentPlaySmall = new FragmentPlayBig();
            switchContent(R.id.frame, fragmentPlaySmall);
        }
    }*/
}
