package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(
    private var forecastList: List<DailyForecast>,
    private val onForecastClick: (DailyForecast) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.dateText)
        private val tempText: TextView = view.findViewById(R.id.tempText)
        private val conditionText: TextView = view.findViewById(R.id.conditionText)
        private val detailsButton: Button = view.findViewById(R.id.detailsButton)

        fun bind(forecast: DailyForecast) {
            dateText.text = forecast.date
            tempText.text = forecast.temperature
            conditionText.text = forecast.condition

            detailsButton.setOnClickListener {
                onForecastClick(forecast)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount() = forecastList.size

    fun updateData(newForecast: List<DailyForecast>) {
        forecastList = newForecast
        notifyDataSetChanged()
    }
}
