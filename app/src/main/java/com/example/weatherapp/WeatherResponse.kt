package com.example.weatherapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
data class WeatherResponse(
    val forecasts: List<CityForecast>
)

data class CityForecast(
    val location: String,
    val forecast: List<DailyForecast>
)
@Parcelize
data class DailyForecast(
    val date: String,
    val temperature: String,
    val condition: String
):Parcelable
