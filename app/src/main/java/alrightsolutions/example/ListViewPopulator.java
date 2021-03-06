package alrightsolutions.example;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.List;
import java.util.Random;


import shortroid.com.shortroid.ShortRoidPreferences.FileNameException;
import shortroid.com.shortroid.ShortRoidPreferences.ShortRoidPreferences;

import static alrightsolutions.example.MainActivity.isPlayingFromList;
import static alrightsolutions.example.MainActivity.isPlayingFromMain;
import static alrightsolutions.example.MusicService.firstRun;
import static alrightsolutions.example.MusicService.mediaPlayer;

/**
 * Created by 1405089 on 7/15/2016.
 */
public class ListViewPopulator extends RecyclerView.Adapter<ListViewPopulator.ViewHolder>{
    private static final String TAG = "ListViewPopulator";
    private List<String> musicName,musicImage;
    private List<String> musicAdd,musicArtist;
    private List<Integer> musicId;
    private Activity context;
    private TextView musicNameView,musicArtistView;
    private FloatingActionButton musicControl;
    private ShortRoidPreferences shortRoidPreferences;
    static int PROGRESS=0;
    private AppCompatSeekBar seekBar;
    private LinearLayout linearLayout;
    private TextView timer;
    public static int BACKGROUND_COLOR=0x00;
    static String SONG_NAME="";
    static String SONG_ARTIST="";
    static String SONG_IMAGE="";
    public static String SONG_ADDRESS="";
    static Animation animation;
    DisplayMetrics metrics;
    ImageView previous,next;

    ListViewPopulator(Activity context,List<Integer> musicId, List<String> musicName, List<String> musicAdd,List<String> musicArtist,List<String> musicImage)
    {
        this.context=context;
        this.musicId=musicId;
        this.musicName=musicName;
        this.musicAdd=musicAdd;
        this.musicArtist=musicArtist;
        this.musicImage=musicImage;
        try {
            shortRoidPreferences=new ShortRoidPreferences(context,"music");
        } catch (FileNameException e) {
            e.printStackTrace();
        }
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
    public static String ADDRESS="";
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview,parent,false);

        return new ViewHolder(v);
    }


    void something(String s,int h)
    {  // Log.d("log",mediaPlayer.toString());
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
        String s1=NAME;
        if(s1.length()>20) {
            s1 = s1.substring(0, 20);
            s1=s1+"..";
        }
        String s2=ARTIST;
        Log.d("log",NAME +"  "+ ARTIST);
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
        shortRoidPreferences.setPrefString("musicName",ListViewPopulator.NAME);
        shortRoidPreferences.setPrefString("musicArtist",ListViewPopulator.ARTIST);
        shortRoidPreferences.setPrefString("musicArt",ListViewPopulator.IMAGE);
        shortRoidPreferences.setPrefInt("musicPosition", NOW_PLAYING);
        shortRoidPreferences.setPrefInt("musicProgress",PROGRESS);
        isPlayingFromMain=1;
        isPlayingFromList=0;
        notification(NAME);
        //seekBar.setProgress(0);
        //seekBar.setMax(mediaPlayer.getDuration());

    }

   static int temp=0;

    private int i=0;
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
    public static Bitmap bitmapIcon;

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
                    bitmapIcon=bitmap;
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
        //cont_seek();
        //seeker();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MusicActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("image",IMAGE);
                bundle.putString("name", NAME);
                bundle.putString("artist",ARTIST);
                bundle.putInt("from",1);
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
                    {   if(firstRun==1)
                    {
                        something(musicAdd.get(NOW_PLAYING),NOW_PLAYING);
                        firstRun=0;
                    }
                        else{

                        mediaPlayer.start();
                    }

                        musicControl.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_white_24dp));
                        notifyItemChanged(NOW_PLAYING);
                        change();
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
        final Runnable runnable=new Runnable() {

            @Override
            public void run() {
                String time;
              //  Log.d(TAG, "run");
                while(mediaPlayer != null) {
                    if(isPlayingFromList==1)
                    {
                       // isPlayingFromList=0;
                        thread.interrupt();

                        break;
                    }
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
                                Log.d("log_", mediaPlayer.getDuration() + " " + seekBar.getProgress() +"  "+ isPlayingFromList +" " + thread.getId());
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
    int g=0;
    void change(){
            g=1;
            try {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        if (temp != musicAdd.size() - 1)
                            something(musicAdd.get(temp), temp);
                        Log.d("log", "ChangeCalled" + temp + "  " + NOW_PLAYING);
                        temp = temp + 1;
                        seekBar.setProgress(0);
                        seekBar.setMax(mediaPlayer.getDuration());
                        if(HomeActivity.isOn()!=null)
                        {
                            HomeActivity.isOn().startActivity(new Intent(HomeActivity.isOn(),HomeActivity.class));
                            HomeActivity.isOn().overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
                            HomeActivity.isOn().finish();

                        }
                         change();
                    }


                });


            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

    }
    void op(){
    Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
        @Override
        public void onFaceUp() {
            if(shortRoidPreferences.getPrefBoolean("sensorActive")) {
                if (i == 0) {
                    try {
                        mediaPlayer.start();
                        notifyItemChanged(NOW_PLAYING);
                        i = 1;
                        h = 1;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onFaceDown() {
            if(shortRoidPreferences.getPrefBoolean("sensorActive")) {
                if (i == 1) {
                    try {
                        mediaPlayer.pause();
                        notifyItemChanged(NOW_PLAYING);
                        i = 0;
                        h = 0;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });
    
    Sensey.getInstance().startShakeDetection(15, new ShakeDetector.ShakeListener() {
        @Override
        public void onShakeDetected() {
            try {
                if(shortRoidPreferences.getPrefBoolean("sensorActive")) {
                    if (h == 1) {
                        temp = randomG(musicAdd.size() - 1, 0);
                        something(musicAdd.get(temp), temp);
                    }
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
  void notification(String s)
  {
      android.support.v7.app.NotificationCompat.Builder mBuilder = new android.support.v7.app.NotificationCompat.Builder(context);
      mBuilder.setSmallIcon(R.mipmap.ic_launcher);
      mBuilder.setContentTitle("Sensomizik");
      mBuilder.setContentText(s);
      mBuilder.setAutoCancel(false);
      Intent resultIntent = new Intent(context, MainActivity.class);
      TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
      stackBuilder.addParentStack(MainActivity.class);
      stackBuilder.addNextIntent(resultIntent);
      PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
      mBuilder.setContentIntent(resultPendingIntent);;
      NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

      mNotificationManager.notify(101,mBuilder.build());
  }
}
