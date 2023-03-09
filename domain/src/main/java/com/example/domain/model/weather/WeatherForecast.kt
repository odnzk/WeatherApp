package com.example.domain.model.weather

data class WeatherForecast(
    val city: City,
    val list: List<WeatherInfo>
)
