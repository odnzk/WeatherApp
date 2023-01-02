package com.example.weatherapp.app.presentation.util.icons

import androidx.annotation.DrawableRes
import com.example.weatherapp.R

enum class WeatherDayIcons(
    val numberCodes: IntRange,
    @DrawableRes val idDrawable: Int
) {
    THUNDERSTORM(200..232, R.drawable.ic_thunderstorm),
    RAIN(300..531, R.drawable.ic_rain),
    SNOW(600..622, R.drawable.ic_snow),
    MIST(701..781, R.drawable.ic_mist),
    SUN(800..802, R.drawable.ic_sun),
    CLOUDS(803..804, R.drawable.ic_clouds)
}

