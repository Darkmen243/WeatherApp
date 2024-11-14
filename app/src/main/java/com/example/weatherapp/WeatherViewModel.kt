package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    val weatherData: LiveData<WeatherResponse> = WeatherRepository.weatherResponse
    val errorMessage: LiveData<String> = WeatherRepository.errorMessage
    val lastUpdated: LiveData<String> = WeatherRepository.lastUpdated

    fun loadWeatherData() {
        viewModelScope.launch {
            WeatherRepository.fetchLatestWeatherData()
        }
    }
}
