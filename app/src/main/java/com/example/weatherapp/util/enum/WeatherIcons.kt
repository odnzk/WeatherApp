package com.example.weatherapp.util.enum

import androidx.annotation.DrawableRes
import com.example.weatherapp.R

enum class WeatherIcons(
    val numberCodes: IntRange,
    val timeStateChar: Char,
    @DrawableRes val idDrawable: Int
) {
    THUNDERSTORM(200..232, 'd', R.drawable.ic_thunderstorm),
    DRIZZLE(300..321, 'd', R.drawable.ic_drizzle),
    RAIN(500..531, 'd', R.drawable.ic_rain),
    SNOW(600..622, 'd', R.drawable.ic_snow),
    MIST(701..781, 'd', R.drawable.ic_mist),
    CLOUDS_WITH_SUN(801..802, 'd', R.drawable.ic_sunclouds),
    CLOUDS(803..804, 'd', R.drawable.ic_clouds),
    SUN(800..800, 'd', R.drawable.ic_sun),
    MOON(800..800, 'n', R.drawable.ic_moon),
    MOON_WITH_CLOUDS(801..804, 'n', R.drawable.ic_moonclouds)
}
