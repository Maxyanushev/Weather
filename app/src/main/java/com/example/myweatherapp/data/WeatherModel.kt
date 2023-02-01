package com.example.myweatherapp.data


import com.fasterxml.jackson.annotation.JsonProperty

data class WeatherModel(
    @JsonProperty("base")
    val base: String, // stations
    @JsonProperty("clouds")
    val clouds: Clouds,
    @JsonProperty("cod")
    val cod: Int, // 200
    @JsonProperty("coord")
    val coord: Coord,
    @JsonProperty("dt")
    val dt: Int, // 1674847584
    @JsonProperty("id")
    val id: Int, // 2643743
    @JsonProperty("main")
    val main: Main,
    @JsonProperty("name")
    val name: String, // London
    @JsonProperty("sys")
    val sys: Sys,
    @JsonProperty("timezone")
    val timezone: Int, // 0
    @JsonProperty("visibility")
    val visibility: Int, // 10000
    @JsonProperty("weather")
    val weather: List<Weather>,
    @JsonProperty("wind")
    val wind: Wind
) {
    data class Clouds(
        @JsonProperty("all")
        val all: Int // 75
    )

    data class Coord(
        @JsonProperty("lat")
        val lat: Double, // 51.5085
        @JsonProperty("lon")
        val lon: Double // -0.1257
    )

    data class Main(
        @JsonProperty("feels_like")
        val feelsLike: Double, // 275.27
        @JsonProperty("humidity")
        val humidity: Int, // 84
        @JsonProperty("pressure")
        val pressure: Int, // 1030
        @JsonProperty("temp")
        val temp: Double, // 276.6
        @JsonProperty("temp_max")
        val tempMax: Double, // 278.01
        @JsonProperty("temp_min")
        val tempMin: Double // 274.05
    )

    data class Sys(
        @JsonProperty("country")
        val country: String, // GB
        @JsonProperty("id")
        val id: Int, // 2075535
        @JsonProperty("sunrise")
        val sunrise: Int, // 1674805597
        @JsonProperty("sunset")
        val sunset: Int, // 1674837568
        @JsonProperty("type")
        val type: Int // 2
    )

    data class Weather(
        @JsonProperty("description")
        val description: String, // broken clouds
        @JsonProperty("icon")
        val icon: String, // 04n
        @JsonProperty("id")
        val id: Int, // 803
        @JsonProperty("main")
        val main: String // Clouds
    )

    data class Wind(
        @JsonProperty("deg")
        val deg: Int, // 350
        @JsonProperty("speed")
        val speed: Double // 1.54
    )
}