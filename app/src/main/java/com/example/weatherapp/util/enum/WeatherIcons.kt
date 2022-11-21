package com.example.weatherapp.util.enum

import androidx.annotation.DrawableRes
import com.example.weatherapp.R

enum class WeatherDayIcons(
    val numberCodes: IntRange,
    @DrawableRes val idDrawable: Int
) {
    THUNDERSTORM(200..232, R.drawable.ic_thunderstorm),
    DRIZZLE(300..321, R.drawable.ic_drizzle),
    RAIN(500..531, R.drawable.ic_rain),
    SNOW(600..622, R.drawable.ic_snow),
    MIST(701..781, R.drawable.ic_mist),
    SUN(800..800, R.drawable.ic_sun),
    CLOUDS_WITH_SUN(801..802, R.drawable.ic_sunclouds),
    CLOUDS(803..804, R.drawable.ic_clouds)
}

enum class WeatherNightIcons(
    val numberCodes: IntRange,
    @DrawableRes val idDrawable: Int
) {
    MOON(800..800, R.drawable.ic_moon),
    MOON_WITH_CLOUDS(801..804, R.drawable.ic_moonclouds)
}
