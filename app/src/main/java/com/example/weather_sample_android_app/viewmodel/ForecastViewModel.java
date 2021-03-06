package com.example.weather_sample_android_app.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weather_sample_android_app.local.data.DataLocalManager;
import com.example.weather_sample_android_app.location.LocationManager;
import com.example.weather_sample_android_app.model.DataPointModel;
import com.example.weather_sample_android_app.model.Forecast;
import com.example.weather_sample_android_app.network.RestApiClient;
import com.example.weather_sample_android_app.utlis.DateFormatter;
import com.example.weather_sample_android_app.utlis.TemperatureFormatter;
import com.example.weather_sample_android_app.view.activity.MainActivity;
import com.example.weather_sample_android_app.view.adapter.ForecastAdapter;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastViewModel extends BaseViewModel<Forecast> implements LocationListener {
    private final Context context;
    private final RestApiClient restApiClient;
    public LocationManager locationManager;
    private List<ForecastAdapter.ItemViewType> items;

    private boolean showProgressBar;

    private Location lastLocation;

    public ForecastViewModel(Context context) {
        this.context = context;
        locationManager = new LocationManager(context, this);
        restApiClient = new RestApiClient();
        items = Collections.emptyList();
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public void setModelData(Forecast modelData) {
        super.setModelData(modelData);
        if (modelData != null) {
            DataLocalManager.setForecast(modelData);
        }
        setItems();
    }

    public List<ForecastAdapter.ItemViewType> getItems() {
        return items;
    }

    private void setItems() {
        Forecast forecast = DataLocalManager.getForecast();
        items = new ArrayList<>();
        if (forecast != null) {
            CurrentForecast currentForecast = new CurrentForecast();
            currentForecast.setIcon(forecast.getCurrently().getIcon());
            currentForecast.setTemperature(TemperatureFormatter.format(forecast.getCurrently().getTemperature()));
            currentForecast.setSummary(forecast.getCurrently().getSummary());
            currentForecast.setNextHourForecast(forecast.getHourly().getSummary());
            currentForecast.setNext24HoursForecast(forecast.getDaily().getSummary());
            items.add(currentForecast);

            // set daily forecast
            for (DataPointModel item : forecast.getDaily().getData()) {
                DailyForecast dailyForecast = new DailyForecast();
                dailyForecast.setIcon(item.getIcon());
                dailyForecast.setDate(DateFormatter.getDate(item.getTime()));
                dailyForecast.setSummary(item.getSummary());
                items.add(dailyForecast);
            }
        }
        // set current forecast
    }

    public boolean getShowProgressBar() {
        return showProgressBar;
    }

    public void setShowProgressBar(boolean show) {
        showProgressBar = show;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onResume() {
        super.onResume();

        if (!locationManager.hasLocationPermission()) {
            ((AppCompatActivity) context).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LocationManager.PERMISSION_ACCESS_COARSE_LOCATION);
            return;
        }

        locationManager.connect();
        locationManager.checkLocationSettings(result -> {
            final com.google.android.gms.common.api.Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    locationManager.getLocationUpdates();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult((MainActivity) context, LocationManager.PERMISSION_ACCESS_COARSE_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onPause() {
        locationManager.disconnect();
    }

    public void fetchWeatherForecast(Location location) {
        showProgressBar();

        long SECOND_IN_MILLI = 60000;//60000 seconds
        final Handler timerHandler = new Handler();
        final Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                restApiClient.fetchWeatherForecast(location.getLongitude(), location.getLatitude(), new Callback<Forecast>() {
                    @Override
                    public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                        Forecast forecast = response.body();
                        setModelData(forecast);
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<Forecast> call, Throwable t) {
                        hideProgressBar();
                    }
                });
                timerHandler.postDelayed(this, SECOND_IN_MILLI);
            }
        };
        timerHandler.postDelayed(timerRunnable, SECOND_IN_MILLI);
    }

    private void showProgressBar() {
        showProgressBar = true;
        notifyChange();
    }

    private void hideProgressBar() {
        showProgressBar = false;
        notifyChange();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lastLocation = location;
            fetchWeatherForecast(location);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LocationManager.PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.getLocationUpdates();
            }
        }
    }
}


