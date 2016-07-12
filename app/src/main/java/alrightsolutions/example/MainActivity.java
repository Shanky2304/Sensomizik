package alrightsolutions.example;

import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.github.nisrulz.sensey.FlipDetector;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int[] ar={R.raw.ataleof2citiez,R.raw.famous,R.raw.wetdreamz};
        final MediaPlayer mplayer=MediaPlayer.create(this,ar[0]);
        Button button_play= (Button) findViewById(R.id.play);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mplayer.start();
            }
        });
        Button button_pause= (Button) findViewById(R.id.pause);
        button_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mplayer.pause();
            }
        });
        Sensey.getInstance().init(this);
        Sensey.getInstance().startFlipDetection(new FlipDetector.FlipListener() {
            @Override
            public void onFaceUp() {
                if(i==0)
                {
                    mplayer.start();
                    i=1;
                }
            }

            @Override
            public void onFaceDown() {
                if(i==1)
                {
                    mplayer.pause();
                    i=0;
                }
            }
        });

        Sensey.getInstance().startShakeDetection(10,new ShakeDetector.ShakeListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onShakeDetected() {
                shuffle(ar);
                Log.d("ff","fsaffasfqfaf");
                mplayer.selectTrack(ar[0]);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void shuffle(int[] ar){
        int index;
        Random random = new Random();
        for (int i = 0; i < ar.length-1; i++)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                ar[index] ^= ar[i];
                ar[i] ^= ar[index];
                ar[index] ^= ar[i];
            }
        }
    }
}
