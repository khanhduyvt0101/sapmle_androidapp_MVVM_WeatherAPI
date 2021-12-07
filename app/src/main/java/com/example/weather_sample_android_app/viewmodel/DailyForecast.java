package com.example.weather_sample_android_app.viewmodel;

import android.graphics.Typeface;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.weather_sample_android_app.utlis.MeteoconsConverter;
import com.example.weather_sample_android_app.view.adapter.ForecastAdapter;

public class DailyForecast implements ForecastAdapter.ItemViewType {
    private String icon;
    private String date;
    private String summary;

    @BindingAdapter({"app:text"})
    public static void loadMeteocon(TextView view, String text) {
        Typeface meteocons = Typeface.createFromAsset(view.getContext().getAssets(), "meteocons.ttf");
        view.setTypeface(meteocons);
        view.setText(MeteoconsConverter.from(text));
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getItemType() {
        return ForecastAdapter.ItemViewType.ITEM_TYPE_DAILY_FORECAST;
    }
}