package alrightsolutions.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CompoundButton;

import shortroid.com.shortroid.ShortRoidPreferences.FileNameException;
import shortroid.com.shortroid.ShortRoidPreferences.ShortRoidPreferences;

/**
 * Created by JohnConnor on 23-Dec-16.
 */

public class SettingsActivity extends AppCompatActivity {
    ShortRoidPreferences shortRoidPreferences;
    AppCompatCheckBox sensorActive;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        setAllPreferences();
        setClickHandlers();
    }
    void init()
    {
        try {
            shortRoidPreferences=new ShortRoidPreferences(SettingsActivity.this,"music");
        } catch (FileNameException e) {
            e.printStackTrace();
        }
        sensorActive=(AppCompatCheckBox)findViewById(R.id.sensor_active);
    }
    void setAllPreferences()
    {
        if(shortRoidPreferences.getPrefBoolean("sensorActive"))
        {
            sensorActive.setChecked(true);
        }
        else sensorActive.setChecked(false);
    }
    void setClickHandlers()
    {
        sensorActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                shortRoidPreferences.setPrefBoolean("sensorActive",b);
            }
        });
    }
}
