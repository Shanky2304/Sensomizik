package alrightsolutions.example;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import alrightsolutions.example.Model.RecommendedMusic;
import alrightsolutions.example.adapter.RecommendationAdapter;
import alrightsolutions.example.com.devadvance.circularseekbar.CircularSeekBar;
import io.realm.Realm;
import io.realm.RealmResults;
import mohitbadwal.rxconnect.RxConnect;
import shortroid.com.shortroid.ShortAnimation.ShortAnimation;
import shortroid.com.shortroid.ShortRoidPreferences.FileNameException;
import shortroid.com.shortroid.ShortRoidPreferences.ShortRoidPreferences;

import static alrightsolutions.example.ListViewPopulator.NOW_PLAYING;
import static alrightsolutions.example.ListViewPopulator.PROGRESS;
import static alrightsolutions.example.ListViewPopulator.animation;
import static alrightsolutions.example.ListViewPopulator.flag;
import static alrightsolutions.example.MusicService.mediaPlayer;

/**
 * Created by Shanky23 on 2/19/2017.
 */

public class HomeFragment extends android.support.v4.app.Fragment {
    @Nullable
    Context context;
    Button button;
    RecyclerView recyclerView;
    ProgressWheel progressWheel;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter adapter;
    ShortRoidPreferences shortRoidPreferences;
    TextView musicName,musicArtist;
    CircularSeekBar appCompatSeekBar;
    ImageView musicImage;
    LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    public static android.support.v4.app.Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        init(view);
        setRecyclerView();

    }
    void init(View view)
    {
        try {
            shortRoidPreferences=new ShortRoidPreferences(context,"music");
        } catch (FileNameException e) {
            e.printStackTrace();
        }
        //button = new Button(context);
        button = (Button) view.findViewById(R.id.app);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
                if(thread!=null)thread.interrupt();
                HomeActivity.activity=null;
                getActivity().finish();

            }
        });
        linearLayout=(LinearLayout)view.findViewById(R.id.linear_click_home);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_recom);
        progressWheel=(ProgressWheel)view.findViewById(R.id.progress_wheel);
        linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        musicName=(TextView)view.findViewById(R.id.home_music_name);
        musicArtist=(TextView)view.findViewById(R.id.home_music_artist);
        musicImage=(ImageView)view.findViewById(R.id.home_album_art);
        appCompatSeekBar=(CircularSeekBar)view.findViewById(R.id.home_appCompatSeekBar);

        if(shortRoidPreferences.getPrefBoolean("instance"))
        {
            final String image=shortRoidPreferences.getPrefString("musicArt");
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(),MusicActivity.class);
                    Bundle b=new Bundle();
                    b.putString("name",musicName.getText().toString());
                    b.putString("artist",musicArtist.getText().toString());
                    b.putString("image",image);
                    b.putInt("from",0);
                    intent.putExtra("music",b);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
                    thread.interrupt();
                    HomeActivity.activity=null;
                    getActivity().finish();
                }
            });
            musicName.setText(shortRoidPreferences.getPrefString("musicName"));
            musicArtist.setText(shortRoidPreferences.getPrefString("musicArtist"));
            appCompatSeekBar.setMax(shortRoidPreferences.getPrefInt("musicDuration"));
            appCompatSeekBar.setProgress(shortRoidPreferences.getPrefInt("musicProgress"));
            musicImage.setImageDrawable(context.getResources().getDrawable(R.drawable.music));
            if(image!=null&&image.length()>0) {
                Picasso.with(context).load(new File(image)).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        musicImage.setImageBitmap(bitmap);
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
                musicImage.setImageDrawable(context.getResources().getDrawable(R.drawable.music));
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
                animation.setInterpolator(new LinearInterpolator());
                musicImage.startAnimation(animation);

            }
          /*  new Thread(new Runnable() {
                @Override
                public void run() {
                    int i=0;
                    while (i>=0) {
                        if(mediaPlayer!=null) {
                            if (mediaPlayer.isPlaying()) {
                                flag = 1;

                            }
                            cont_seek();
                            break;
                        }
                        i++;
                        if(i==100)
                            i=0;
                    }


                }

            }).start();*/
            cont_seek();
            seeker();
        }
    }
    void setRecyclerView()
    {    Realm.init(context);
        realm=Realm.getDefaultInstance();
        notRefresh();
        RxConnect rxConnect=new RxConnect(getActivity());
        rxConnect.setCachingEnabled(false);
        rxConnect.executeNoParam("http://www.deliverit.co.in/recom.php", new RxConnect.RxResultHelper() {
            @Override
            public void onResult(final String result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Realm.init(context);
                        realm=Realm.getDefaultInstance();
                        if(shortRoidPreferences.getPrefString("recom")!=null && shortRoidPreferences.getPrefString("recom").length()>2)
                        {
                            if(result.contentEquals(shortRoidPreferences.getPrefString("recom")))
                            {
                                //notRefresh();
                            }
                            else refresh(result);
                        }
                        else refresh(result);
                    }
                }).start();

            }

            @Override
            public void onNoResult() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
    List<RecommendedMusic> recommendedMusicList;
    Realm realm;
    void refresh(String refreshed)
    {   recommendedMusicList=new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(RecommendedMusic.class);
            }
        });
        File dir = new File(getActivity().getCacheDir()+"/sensomizik");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
             boolean b=   new File(dir, children[i]).delete();
                Log.d("qwerty",""+b);
            }
        }

        shortRoidPreferences.setPrefString("recom",refreshed);
        try {
            JSONObject jsonObject3=new JSONObject(refreshed);
            JSONArray jsonArray=jsonObject3.getJSONArray("recommendations");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                RecommendedMusic recommendedMusic=new RecommendedMusic();
                recommendedMusic.setId(i);
                recommendedMusic.setAlbum(jsonObject.getString("album"));
                recommendedMusic.setName(jsonObject.getString("name"));
                recommendedMusic.setArtist(jsonObject.getString("artist"));
                recommendedMusic.setInformation(jsonObject.getString("information"));
                recommendedMusic.setImageLink(jsonObject.getString("imageLink"));
                realm.beginTransaction();
                realm.copyToRealm(recommendedMusic);
                realm.commitTransaction();
                recommendedMusicList.add(recommendedMusic);
            }
            adapter=new RecommendationAdapter(getActivity(),recommendedMusicList,1);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(adapter);
                    progressWheel.setVisibility(View.GONE);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void notRefresh()
    {
        RealmResults<RecommendedMusic> recommendedMusics;
        recommendedMusics=realm.where(RecommendedMusic.class).findAll();
        int count=0;
        File dir = new File(getActivity().getCacheDir()+"/sensomizik");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
               count++;
            }
        }
        Log.d("qwerty",""+count);
        if(count!=recommendedMusics.size()) {
            shortRoidPreferences.setPrefString("recom","");
            System.gc();
            return;
        }

        if(recommendedMusics.size()>0)
        {
            List<RecommendedMusic> list=recommendedMusics;
            adapter=new RecommendationAdapter(getActivity(),list,0);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(adapter);
                    progressWheel.setVisibility(View.GONE);
                }
            });
        }
    }
    int progress;
    public int isPlayingFromList=0;
    void cont_seek() {

        if (mediaPlayer != null) {
            appCompatSeekBar.setMax(mediaPlayer.getDuration());

            appCompatSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        }
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                String time;
                Log.d("log", "run2");
                while (mediaPlayer != null) {
                    if (isPlayingFromList == 1) {
                        isPlayingFromList = 0;
                        thread.interrupt();
                        break;
                    }
                    if (mediaPlayer.isPlaying()) {

                        progress= mediaPlayer.getCurrentPosition();
                        int min = (progress / 1000) / 60;
                        //        Log.d("auto", "seek");
                        int sec = (progress / 1000) % 60;
                        if (sec < 10)
                            time = "0" + sec;
                        else
                            time = "" + sec;
                        final String elapsedTime = min + ":" + time + "";
                        if(getActivity()==null)
                        {
                            thread.interrupt();
                            break;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // timer.setText(elapsedTime);

                                Log.d("log_1", mediaPlayer.getDuration() + " " + appCompatSeekBar.getProgress());
                                appCompatSeekBar.setMax(mediaPlayer.getDuration());
                                appCompatSeekBar.setProgress(progress);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ////
                    } else {
                        if (flag == 0) {

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
        thread = new Thread(runnable);
        thread.start();

    }
    Thread thread;

    void seeker(){
        appCompatSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
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
    }

}
