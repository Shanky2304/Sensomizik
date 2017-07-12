package alrightsolutions.example;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;

import shortroid.com.shortroid.ShortRoidPreferences.FileNameException;
import shortroid.com.shortroid.ShortRoidPreferences.ShortRoidPreferences;

import static alrightsolutions.example.ListViewPopulator.ARTIST;
import static alrightsolutions.example.ListViewPopulator.NAME;
import static alrightsolutions.example.ListViewPopulator.bitmapIcon;

/**
 * Created by JohnConnor on 18-Dec-16.
 */

public class MusicService extends Service{
    @Nullable
    public static MediaPlayer mediaPlayer;
    public static int firstRun=0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    ShortRoidPreferences shortRoidPreferences;
    @Override
    public void onCreate() {
        mediaPlayer=new MediaPlayer();

        try {
            shortRoidPreferences=new ShortRoidPreferences(getApplicationContext(),"music");
            if(shortRoidPreferences.getPrefBoolean("instance"))
            {
                firstRun=1;
            }
            if(shortRoidPreferences.getPrefString("musicAddress")!=null)
            mediaPlayer=MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(shortRoidPreferences.getPrefString("musicAddress"))));
        } catch (FileNameException e) {
            e.printStackTrace();
        }
        Log.d("log","music created");
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("log","music created2");
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }
    void notification()
    {
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);



        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.music)

                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setBitmap(R.id.album_art,"",bitmapIcon);
        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.data,NAME);
        remoteViews.setTextViewText(R.id.data_album,ARTIST);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }
}
