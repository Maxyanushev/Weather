package com.example.myweatherapp.data.repositories

import com.example.myweatherapp.data.WeatherAPI
import com.example.myweatherapp.model.WeatherModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val api: WeatherAPI) {

    suspend fun getData(cityName: String): Response<WeatherModel?> = api.getData(cityName = cityName)
}