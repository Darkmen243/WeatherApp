package com.example.weatherapp
import retrofit2.http.GET

interface RetroFit {
    @GET("weather-forecast")
    suspend fun getWeatherData(): WeatherResponse
}
