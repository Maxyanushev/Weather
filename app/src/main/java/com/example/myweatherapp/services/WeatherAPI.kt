package com.example.myweatherapp.services

import com.example.myweatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather?q=lozova&APPID=4c7e6e9a8f6d2425a4cd9757833e15e5")
    fun getData(
        @Query("q") cityName: String
    ): Single<WeatherModel>
}