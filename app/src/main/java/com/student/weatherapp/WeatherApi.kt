package com.student.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getWeeklyForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeeklyForecastResponse
}

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val wind: Int,
    val pressure: Int,
    val humidity: Int,
)
data class Wind(
    val speed: Int
)
data class Weather(
    val description: String,
    val icon: String
)

data class WeeklyForecastResponse(
    val list: List<Forecast>
)

data class Forecast(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)