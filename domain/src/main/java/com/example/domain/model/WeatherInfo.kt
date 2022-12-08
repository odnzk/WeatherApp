package com.example.domain.model

data class WeatherInfo(
    val dt: Long,
    val dt_txt: String,
    val main: Main,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
