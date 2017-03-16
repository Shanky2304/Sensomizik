package alrightsolutions.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import alrightsolutions.example.R;

/**
 * Created by Shanky23 on 1/16/2017.
 */

public class PlaylistDialogFragment {

    public void showDialog(Activity activity, final PlaylistDialogListener playlistDialogListener){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.playlist_dialog);
        final EditText editText=(EditText)dialog.findViewById(R.id.playlist_name);
        Button button=(Button)dialog.findViewById(R.id.accept);
        Button button1=(Button)dialog.findViewById(R.id.cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistDialogListener.onPositiveClick(editText.getText().toString());
                dialog.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playlistDialogListener.onNegativeClick();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public interface PlaylistDialogListener{
        void onPositiveClick(String s);
        void onNegativeClick();
    }
}
/*public class PlaylistDialogFragment extends DialogFragment {

    public PlaylistDialogFragment(){}

    public interface PlaylistDialogListener{
        void onPositiveClick(String s);
        void onNegativeClick();
    }

    PlaylistDialogListener mListener;
    @Override
    public void onAttach(Context context){

        super.onAttach(context);

        try{
            mListener = (PlaylistDialogListener) context;
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        EditText editText=(EditText)layoutInflater.inflate(R.layout.playlist_dialog,null).findViewById(R.id.playlist_name);
        builder.setView(layoutInflater.inflate(R.layout.playlist_dialog,null)).setMessage("Create a playlist")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onPositiveClick("");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onNegativeClick();
                    }
                });
        return builder.create();
    }




}*/


