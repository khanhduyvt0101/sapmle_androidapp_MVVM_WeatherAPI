package com.example.weather_sample_android_app.application;

import android.app.Application;

import com.example.weather_sample_android_app.local.data.DataLocalManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
