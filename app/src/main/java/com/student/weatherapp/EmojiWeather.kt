package com.student.weatherapp

fun getWeatherEmoji(description: String): String {
    return when {
        description.contains("дождь", ignoreCase = true) -> "🌧"
        description.contains("гроза", ignoreCase = true) -> "⛈"
        description.contains("снег", ignoreCase = true) -> "🌨"
        description.contains("облачно", ignoreCase = true) -> "☁"
        description.contains("ясно", ignoreCase = true) -> "☀"
        description.contains("туман", ignoreCase = true) -> "🌫"
        description.contains("пасмурно", ignoreCase = true) -> "🌥"
        description.contains("переменная облачность", ignoreCase = true) -> "⛅"
        description.contains("морось", ignoreCase = true) -> "🌦"
        description.contains("град", ignoreCase = true) -> "🌨"
        description.contains("метель", ignoreCase = true) -> "🌨"
        description.contains("солнечно", ignoreCase = true) -> "☀"
        else -> "🌤"
    }
}