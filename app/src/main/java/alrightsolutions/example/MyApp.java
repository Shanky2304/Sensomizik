package alrightsolutions.example;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shanky23 on 9/28/2016.
 */

public class MyApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApp.context;
    }
}
