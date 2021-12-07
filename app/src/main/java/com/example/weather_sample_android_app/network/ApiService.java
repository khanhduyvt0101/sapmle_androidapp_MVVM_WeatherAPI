package com.example.weather_sample_android_app.network;


import com.example.weather_sample_android_app.model.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET(RestApiClient.API_KEY + "/{longitude},{latitude}")
    Call<Forecast> getWeatherForecast(@Path("latitude") double latitude, @Path("longitude") double longitude);
}