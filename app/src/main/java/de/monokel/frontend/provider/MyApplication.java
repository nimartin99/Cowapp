package de.monokel.frontend.provider;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class has the purpose to provide the Context-object of this app. Therefor there is the static methode getAppContext()
 *
 * @author Mergim Miftari
 */
public class MyApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
