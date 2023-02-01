package com.example.myweatherapp.data.repositories

import com.example.myweatherapp.data.Resource
import com.example.myweatherapp.data.WeatherAPI
import com.example.myweatherapp.data.WeatherModel
import com.example.myweatherapp.data.call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val api: WeatherAPI) {

    suspend fun getData(cityName: String): Resource<WeatherModel?> = call { api.getData(cityName = cityName) }
}