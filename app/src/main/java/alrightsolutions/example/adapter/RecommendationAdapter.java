package alrightsolutions.example.adapter;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import alrightsolutions.example.Model.RecommendedMusic;
import alrightsolutions.example.R;

/**
 * Created by JohnConnor on 16-Mar-17.
 */

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    Activity context;
    List<RecommendedMusic> recommendedMusicList;
    int refresh;
    public RecommendationAdapter(Activity context,List<RecommendedMusic> recommendedMusicList,int refresh)
    {
        this.context=context;
        this.recommendedMusicList=recommendedMusicList;
        this.refresh=refresh;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_item,parent,false);
        return new ViewHolder(v);
    }

    void setImage(final ImageView image, String s, final LinearLayout linearLayout, final String name)
    {
        Log.d("qwerty","refresh " + refresh + s);
        if(refresh==0)
        {
            Picasso.with(context).load(new File(context.getCacheDir()+"/sensomizik/"+name+".png")).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    image.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int primaryDark = context.getResources().getColor(R.color.colorPrimaryDark);
                            int primary = context.getResources().getColor(R.color.colorPrimary);
                            int x = palette.getDarkVibrantColor(primary);
                            linearLayout.setBackgroundColor(x);

                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    image.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        else{
            Picasso.with(context).load(s).into(new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    image.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int primaryDark = context.getResources().getColor(R.color.colorPrimaryDark);
                            int primary = context.getResources().getColor(R.color.colorPrimary);
                            int x = palette.getDarkVibrantColor(primary);
                            linearLayout.setBackgroundColor(x);
                            saveImage(bitmap,name);

                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                        image.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }
    void saveImage(final Bitmap bitmap, final String name)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File fr=new File(context.getCacheDir()+"/sensomizik");
                fr.mkdirs();
                File f = new File(fr,name+".png" );
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f, false);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        }).start();

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RecommendedMusic recommendedMusic=recommendedMusicList.get(position);
        holder.musicName.setText(recommendedMusic.getName());
        holder.musicArtist.setText(recommendedMusic.getArtist());
        setImage(holder.musicImage,recommendedMusic.getImageLink(),holder.musicColor,recommendedMusic.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString="https://www.google.com/search?q=download "+
                        recommendedMusic.getArtist()
                        +" "+recommendedMusic.getName()+" mp3";
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedMusicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    TextView musicName,musicArtist;
    ImageView musicImage;
    LinearLayout musicColor;
    public ViewHolder(View itemView) {
        super(itemView);
        musicName=(TextView)itemView.findViewById(R.id.music_recom_name);
        musicArtist=(TextView)itemView.findViewById(R.id.music_recom_artist);
        musicImage=(ImageView)itemView.findViewById(R.id.music_recom_image);
        musicColor=(LinearLayout)itemView.findViewById(R.id.recom_color);

    }
}
}
