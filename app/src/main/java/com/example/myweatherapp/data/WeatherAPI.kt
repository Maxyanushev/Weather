package com.example.myweatherapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather")
    suspend fun getData(
        @Query("q") cityName: String,
        @Query("APPID") id: String = "4c7e6e9a8f6d2425a4cd9757833e15e5",
    ): Response<WeatherModel>

    @GET("/data/2.5/weather")
    suspend fun getWeather(@Query("id") id: String, @Query("appid") appId: String = "4c7e6e9a8f6d2425a4cd9757833e15e5"):Response<WeatherModel>
}