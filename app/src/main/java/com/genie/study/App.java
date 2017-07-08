package com.genie.study;

import android.app.Application;

/**
 * Created by Genie on 7/8/2017.
 */

public class App extends Application {
    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
