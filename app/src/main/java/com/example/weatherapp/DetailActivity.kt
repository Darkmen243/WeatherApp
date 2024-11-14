package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val forecast = intent.getParcelableExtra<DailyForecast>("DAILY_FORECAST")
        val dateText = findViewById<TextView>(R.id.dateText)
        val tempText = findViewById<TextView>(R.id.tempText)
        val conditionText = findViewById<TextView>(R.id.conditionText)
        val backButton = findViewById<Button>(R.id.backButton)

        forecast?.let {
            dateText.text = it.date
            tempText.text = it.temperature
            conditionText.text = it.condition
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}
