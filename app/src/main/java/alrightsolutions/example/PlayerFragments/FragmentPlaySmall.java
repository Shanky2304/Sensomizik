package alrightsolutions.example.PlayerFragments;

import android.annotation.TargetApi;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import alrightsolutions.example.ListViewPopulator;
import alrightsolutions.example.R;

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
        relativeLayout=(RelativeLayout)rootView.findViewById(R.id.rel_play);
        musicImage=(ImageView)rootView.findViewById(R.id.music_art);

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
        if(ListViewPopulator.SONG_IMAGE.length()>0)
        setMusicImage(musicImage,ListViewPopulator.SONG_IMAGE);
        play.setBackground(getResources().getDrawable(R.drawable.pause));
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (k>0)
                {
                    try {
                        Thread.sleep(1000);
                        if(ListViewPopulator.mediaPlayer.isPlaying())
                        {   if(f==1) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    play.setBackground(getResources().getDrawable(R.drawable.pause));
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
                                    play.setBackground(getResources().getDrawable(R.drawable.play));
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
        }).start();
        return rootView;
    }
    void setMusicImage(final ImageView image, String s)
    {
        Picasso.with(getActivity()).load(new File(s)).into(image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                image.setImageResource(R.color.colorPrimaryDark);
            }
        });
    }
}
