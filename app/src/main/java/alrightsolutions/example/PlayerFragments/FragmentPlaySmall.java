package alrightsolutions.example.PlayerFragments;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import alrightsolutions.example.ListViewPopulator;
import alrightsolutions.example.R;

/**
 * Created by JohnConnor on 30-Jul-16.
 */
public class FragmentPlaySmall extends Fragment {
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
    String s;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_play, container, false);
        title=(TextView)rootView.findViewById(R.id.music_name);
        album=(TextView)rootView.findViewById(R.id.music_artist);
        play=(Button)rootView.findViewById(R.id.music_play);
        if(ListViewPopulator.SONG_NAME.length()>27)
        {
            s=ListViewPopulator.SONG_NAME.substring(0,27);
            s=s+"....";
        }
        title.setText(s);
        album.setText(ListViewPopulator.SONG_ARTIST);
        return rootView;
    }
}
