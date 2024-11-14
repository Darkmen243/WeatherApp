package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var citySpinner: Spinner
    private lateinit var lastUpdatedText: TextView
    private val updateIntervalMillis = 20000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        weatherAdapter = WeatherAdapter(listOf()) { dailyForecast ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("DAILY_FORECAST", dailyForecast)
            startActivity(intent)
        }
        recyclerView.adapter = weatherAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        citySpinner = findViewById(R.id.citySpinner)
        lastUpdatedText = findViewById(R.id.lastUpdatedText)
        setupCitySpinner()

        weatherViewModel.weatherData.observe(this) { weatherData ->
            val cityNames = weatherData.forecasts.map { it.location }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            citySpinner.adapter = adapter
            updateForecastForCity(weatherData.forecasts.firstOrNull())
        }

        weatherViewModel.lastUpdated.observe(this) { timestamp ->
            lastUpdatedText.text = timestamp
            Snackbar.make(recyclerView, "Weather updated: $timestamp", Snackbar.LENGTH_SHORT).show()
        }

        weatherViewModel.errorMessage.observe(this) { message ->
            Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
        }

        weatherViewModel.loadWeatherData()
        startWeatherUpdates()
        //setupWorkManager()
    }

    private fun setupCitySpinner() {
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCity = parent.getItemAtPosition(position) as String
                weatherViewModel.weatherData.value?.let { data ->
                    val cityForecast = data.forecasts.find { it.location == selectedCity }
                    updateForecastForCity(cityForecast)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateForecastForCity(cityForecast: CityForecast?) {
        cityForecast?.let {
            weatherAdapter.updateData(it.forecast)
        }
    }
    //работает с задержкой
    private fun setupWorkManager() {
        Snackbar.make(
            findViewById(R.id.recyclerView),
            "Setting up periodic weather updates with WorkManager",
            Snackbar.LENGTH_SHORT
        ).show()

        val weatherUpdateRequest = PeriodicWorkRequestBuilder<WeatherUpdateWorker>(20, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "WeatherUpdate",
            ExistingPeriodicWorkPolicy.KEEP,
            weatherUpdateRequest
        )
        Snackbar.make(
            findViewById(R.id.recyclerView),
            "WorkManager request has been enqueued",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun startWeatherUpdates() {
        CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                loadWeatherData()
                delay(updateIntervalMillis)
            }
        }
    }

    private fun loadWeatherData() {
        try {
            weatherViewModel.loadWeatherData()
            Snackbar.make(findViewById(R.id.recyclerView), "Weather data updated successfully", Snackbar.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Snackbar.make(findViewById(R.id.recyclerView), "Failed to update weather", Snackbar.LENGTH_LONG).show()
        }
    }
}
