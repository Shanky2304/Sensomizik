package alrightsolutions.example;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Shanky23 on 2/19/2017.
 */

public class HomeFragment extends android.support.v4.app.Fragment {
    @Nullable
    Context context;
    Button button;
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
        //button = new Button(context);
        button = (Button) view.findViewById(R.id.app);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
