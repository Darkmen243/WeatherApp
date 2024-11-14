package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeatherRepository {

    private val _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse> get() = _weatherResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _lastUpdated = MutableLiveData<String>()
    val lastUpdated: LiveData<String> get() = _lastUpdated

    suspend fun fetchLatestWeatherData() {
        try {
            val response = WeatherAPIService.api.getWeatherData()
            _weatherResponse.postValue(response)

            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            _lastUpdated.postValue("Last updated: $timestamp")

            Log.d("WeatherRepository", "Weather data updated at $timestamp")
        } catch (e: Exception) {
            _errorMessage.postValue("Failed to fetch weather data")
            Log.e("WeatherRepository", "Error fetching weather data", e)
        }
    }
}
