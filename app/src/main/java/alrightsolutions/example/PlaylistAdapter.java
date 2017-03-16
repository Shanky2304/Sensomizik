package alrightsolutions.example;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import alrightsolutions.example.Model.Music;
import alrightsolutions.example.Model.Playlist;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import shortroid.com.shortroid.ShortAnimation.ShortAnimation;
import shortroid.com.shortroid.ShortRoidDB.ShortRoidDB;

/**
 * Created by Shanky23 on 2/18/2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    Playlist playlist= new Playlist();
    private List<String> musicName,musicImage;
    private List<String> musicAdd,musicArtist;
    private List<Integer> musicId;
    private Context context;
    private String playlistname;
    private boolean checkselected[];
    //RealmConfiguration realmConfig;
   // Realm realm;
    RealmResults<Playlist> playlists;
    private PlaylistSelector playlistselector;

    PlaylistAdapter(Activity context, List<Integer> musicId, List<String> musicName, List<String> musicAdd, List<String> musicArtist,List<String> musicImage, String playlistname ){
        this.musicId = musicId;
        this.musicName = musicName;
        this.musicAdd = musicAdd;
        this.musicArtist = musicArtist;
        this.musicImage = musicImage;
        this.playlistname = playlistname;
        this.context = context;
        checkselected = new boolean[musicId.size()];
    }
    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview,parent,false);
        //realmConfig = new RealmConfiguration.Builder(PlaylistAdapter.this).deleteRealmIfMigrationNeeded().build();
        //realm = Realm.getInstance(realmConfig);
        return new PlaylistViewHolder(v);
    }

    public static Bitmap bitmapIcon;

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, final int position) {
        final String s = musicAdd.get(position);
        final String v = musicName.get(position);
        final String artist=musicArtist.get(position);
        final String image=musicImage.get(position);
        final int id=musicId.get(position);

        if(!checkselected[position]) {
            holder.selected.setVisibility(View.INVISIBLE);
        }
        else{

            holder.selected.setVisibility(View.VISIBLE);
        }

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

        holder.data.setText(v);
        holder.data_album.setText(artist);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlist.setPlaylistname(playlistname);
                playlist.setId(id);
                playlist.setAlbumArt(image);
                playlist.setMusicName(v);
                playlist.setMusicArtist(artist);
                if(!checkselected[holder.getAdapterPosition()]) {
                    ShortAnimation.FadeOutAnimation fadeInAnimation=new ShortAnimation.FadeOutAnimation(holder.selected);
                    fadeInAnimation.startAnimation(500, new ShortAnimation.ShortAnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            holder.selected.setVisibility(View.VISIBLE);
                            checkselected[holder.getAdapterPosition()] = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                }
                else{
                    ShortAnimation.FadeInAnimation fadeOutAnimation=new ShortAnimation.FadeInAnimation(holder.selected);
                    fadeOutAnimation.startAnimation(500, new ShortAnimation.ShortAnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            holder.selected.setVisibility(View.INVISIBLE);
                            checkselected[holder.getAdapterPosition()] = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return musicName.size();
    }


    class PlaylistViewHolder extends RecyclerView.ViewHolder{

        TextView data,data_album;
        ImageView albumArt,selected;
        public PlaylistViewHolder(View itemView) {
            super(itemView);

            data = (TextView) itemView.findViewById(R.id.data);
            data_album = (TextView) itemView.findViewById(R.id.data_album);
            selected = (ImageView) itemView.findViewById(R.id.selected);
            albumArt = (ImageView) itemView.findViewById(R.id.album_art);
        }
    }
}
