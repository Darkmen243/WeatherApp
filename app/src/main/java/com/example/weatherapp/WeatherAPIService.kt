package com.example.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherAPIService {
    private const val BASE_URL = "https://mej1g.wiremockapi.cloud/"

    val api: RetroFit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetroFit::class.java)
    }
}
