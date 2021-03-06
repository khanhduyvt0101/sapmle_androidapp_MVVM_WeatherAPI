package com.example.weather_sample_android_app.utlis;

import android.text.TextUtils;

/**
 * http://quickwebresources.com/free-download-free-weather-icon-set
 * Created by dkang on 5/12/15.
 */
public class MeteoconsConverter {
    public static String from(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }

        if (name.equals("clear-day")) {
            return "B";
        } else if (name.equals("clear-night")) {
            return "1";
        } else if (name.equals("partly-cloudy-day")) {
            return "H";
        } else if (name.equals("partly-cloudy-night")) {
            return "I";
        } else if (name.equals("cloudy")) {
            return "N";
        } else if (name.equals("rain")) {
            return "R";
        } else {
            return ")";
        }
    }
}
