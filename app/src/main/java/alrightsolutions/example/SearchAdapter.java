package alrightsolutions.example;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import alrightsolutions.example.PlayerFragments.FragmentPlayBig;
import alrightsolutions.example.PlayerFragments.FragmentPlaySmall;

/**
 * Created by JohnConnor on 28-Sep-16.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private Activity context;
    private Activity mainContext;
    private List<String> musicName,musicImage;
    private List<String> musicAdd,musicArtist;
    public static String Searched;

    public SearchAdapter(Activity context, List<String> musicName, List<String> musicAdd,List<String> musicArtist,List<String> musicImage,Activity mainContext) {
        this.context = context;
        this.musicName = musicName;
        this.musicAdd = musicAdd;
        this.mainContext=mainContext;
        this.musicImage=musicImage;
        this.musicArtist = musicArtist;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.search_item_adapter,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String s = musicAdd.get(position);
        final String v = musicName.get(position);
        final String artist=musicArtist.get(position);
        final String image=musicImage.get(position);
        holder.songName.setText(v);
        holder.artistName.setText(artist);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{ListViewPopulator.mediaPlayer.stop();}catch (Exception e){e.printStackTrace();}
                 ListViewPopulator.mediaPlayer= MediaPlayer.create(context, Uri.fromFile(new File(s)));
                ListViewPopulator.mediaPlayer.start();
                ListViewPopulator.SONG_ADDRESS=s;
                ListViewPopulator.SONG_ARTIST=artist;
                ListViewPopulator.SONG_IMAGE=image;
                ListViewPopulator.SONG_NAME=v;
               /// fragmentJump(0);
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicName.size();
    }
    public void switchContent(int id, Fragment fragment) {
        if (mainContext == null)
            return;
        if (mainContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mainContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);

        }

    }
   /* private void fragmentJump(int flag) {
        if(flag==0) {
            FragmentPlaySmall fragmentPlaySmall = new FragmentPlaySmall();
            switchContent(R.id.frame, fragmentPlaySmall);
        }
        else {
            FragmentPlayBig fragmentPlaySmall = new FragmentPlayBig();
            switchContent(R.id.frame, fragmentPlaySmall);
        }
    }*/
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView songName, artistName;
        public ViewHolder(View itemView) {
            super(itemView);
            songName=(TextView)itemView.findViewById(R.id.song_name);
            artistName=(TextView)itemView.findViewById(R.id.artist_Name);
        }
    }

}
