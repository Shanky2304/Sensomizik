package alrightsolutions.example;

import android.app.Activity;
import android.app.Application;



/**
 * Created by JohnConnor on 28-Sep-16.
 */

public class MyApplication extends Application{
    private static MyApplication context;
    private static MainActivity mainActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;

    }
    public static void setMainActivity(MainActivity mainActivity)
    {
        MyApplication.mainActivity=mainActivity;
    }
    public static MainActivity getMainContext() {
        return mainActivity;
    }

    public static MyApplication getContext() {
        return context;
    }

}
