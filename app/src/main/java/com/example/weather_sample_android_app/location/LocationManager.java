package com.example.weather_sample_android_app.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.util.concurrent.TimeUnit;

public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long UPDATE_INTERVAL = TimeUnit.HOURS.toMillis(1);
    private static final long FASTEST_INTERVAL = TimeUnit.MINUTES.toMillis(5);
    public static int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private final Context context;
    private final GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private final LocationListener locationListener;

    public LocationManager(Context context, LocationListener locationListener) {
        this.context = context;
        this.locationListener = locationListener;

        googleApiClient = new GoogleApiClient.Builder(this.context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void getLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        } catch (SecurityException e) {
            Log.e(context.getAttributionTag(),e.toString());
        }
    }

    public void checkLocationSettings(ResultCallback<LocationSettingsResult> resultResultCallback) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request)
                .setResultCallback(resultResultCallback);
    }

    public boolean isConnected() {
        if (googleApiClient != null) {
            return googleApiClient.isConnected();
        } else {
            return false;
        }
    }

    public void connect() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void disconnect() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    public void onConnected(@Nullable Bundle bundle) {
        try {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            locationListener.onLocationChanged(lastLocation);
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}