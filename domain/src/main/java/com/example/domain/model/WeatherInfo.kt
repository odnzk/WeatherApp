package com.example.domain.model


data class WeatherInfo(
    val dt: Long,
    val main: Main,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
