package com.jonatan_vahlberg.gallery;

import android.app.Application;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Singleton.shared.load(getApplicationContext());
    }
}
