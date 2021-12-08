package com.example.weather_sample_android_app.local.data;

import android.content.Context;

import com.example.weather_sample_android_app.model.Forecast;
import com.google.gson.Gson;

public class DataLocalManager {
    private static final String PREF_OBJECT_FORECAST = "PREF_OBJECT_FORECAST";
    private static DataLocalManager instance;
    private MyShareReferences myShareReferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.myShareReferences = new MyShareReferences(context);
    }

    public static DataLocalManager getInstance() {
        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static Forecast getForecast() {
        String strJsonForecast = DataLocalManager.getInstance().myShareReferences.getStringValue(PREF_OBJECT_FORECAST);
        Gson gson = new Gson();
        return gson.fromJson(strJsonForecast, Forecast.class);
    }

    public static void setForecast(Forecast forecast) {
        Gson gson = new Gson();
        String strJsonForecast = gson.toJson(forecast);
        DataLocalManager.getInstance().myShareReferences.putStringValue(PREF_OBJECT_FORECAST, strJsonForecast);
    }
}
