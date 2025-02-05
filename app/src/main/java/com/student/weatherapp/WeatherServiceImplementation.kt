package com.student.weatherapp

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherServiceImplementation {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: WeatherService = retrofit.create(WeatherService::class.java)

    suspend fun getCurrentWeather(city: String, apiKey: String): WeatherResponse? {
        return try {
            Log.d("WeatherAPI", "Запрос текущей погоды для города: $city")
            val response = service.getCurrentWeather(city, apiKey)
            Log.d("WeatherAPI", "Получен ответ: $response")
            Log.d("WeatherAPI", "Влажность: ${response.main.humidity}")
            Log.d("WeatherAPI", "Давление: ${response.main.pressure}")
            Log.d("WeatherAPI", "Ветер: ${response.main.wind}")
            response
        } catch (e: Exception) {
            Log.e("WeatherAPI", "Ошибка при получении текущей погоды", e)
            throw Exception("Ошибка при получении погоды: ${e.message}")
        }
    }

    suspend fun getWeeklyForecast(city: String, apiKey: String): WeeklyForecastResponse? {
        return try {
            Log.d("WeatherAPI", "Запрос прогноза на неделю для города: $city")
            val response = service.getWeeklyForecast(city, apiKey)
            Log.d("WeatherAPI", "Получен ответ: $response")
            response
        } catch (e: Exception) {
            Log.e("WeatherAPI", "Ошибка при получении прогноза", e)
            throw Exception("Ошибка при получении прогноза: ${e.message}")
        }
    }
}