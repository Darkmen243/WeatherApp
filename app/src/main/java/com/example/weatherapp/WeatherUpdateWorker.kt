package com.example.weatherapp

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WeatherUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("WeatherUpdateWorker", "Fetching new weather data...")
            WeatherRepository.fetchLatestWeatherData()
            Log.d("WeatherUpdateWorker", "Weather data fetch successful")
            Result.success()
        } catch (e: Exception) {
            Log.e("WeatherUpdateWorker", "Weather data fetch failed", e)
            Result.failure()
        }
    }
}
