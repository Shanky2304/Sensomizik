package alrightsolutions.example;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import alrightsolutions.example.Model.Music;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Shanky23 on 12/24/2016.
 */

public class PlaylistFragment extends Fragment{

    public ArrayList<String> playlist_name;
    TextView tempText;
    ImageView addPlaylist;
    RealmConfiguration realmConfig;
    Realm realm;
    PlaylistSelector selector;
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
     public void onViewCreated(View view, Bundle savedInstanceState){
        GridView gridView = (GridView) getView().findViewById(R.id.playlist_grid);
        gridView.getAdapter();


        PlaylistGridAdapter playlistGridAdapter = new PlaylistGridAdapter(getContext(),playlist_name);
        gridView.setAdapter(playlistGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
     }


    public static Fragment newInstance() {
        return new PlaylistFragment();
    }



    public class PlaylistGridAdapter extends BaseAdapter{

        private final Context gcontext;
        private final ArrayList<String> playlist_name;


        PlaylistGridAdapter(Context context, ArrayList<String> list ) {
            this.gcontext = context;
            this.playlist_name = list;

        }


        @Override
        public int getCount() {
            if(playlist_name == null)
                return 1;
            else
            return playlist_name.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View grid;
            LayoutInflater inflater = (LayoutInflater) gcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(position == 0){

            }
            else {

            }
            createplaylist();
            return tempText;
        }
        void createplaylist() {
            final PlaylistDialogFragment dialogFragment = new PlaylistDialogFragment();
            tempText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogFragment.showDialog(getActivity(), new PlaylistDialogFragment.PlaylistDialogListener() {
                        @Override
                        public void onPositiveClick(String s) {
                            Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                           // MediaStore.Audio.Playlists.
                            Intent intent = new Intent(getActivity().getApplicationContext(),PlaylistSelector.class);
                            intent.putExtra("playlistname",s);
                            try{
                            playlist_name.add(s);
                            }
                            catch (NullPointerException ne){
                                ne.printStackTrace();
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onNegativeClick() {

                        }
                    });

                }
            });
        }
    }


}
