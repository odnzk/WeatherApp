package com.example.weatherapp.app.presentation.util.icons

import androidx.annotation.DrawableRes
import com.example.weatherapp.R

enum class WeatherNightIcons(
    val numberCodes: IntRange,
    @DrawableRes val idDrawable: Int
) {
    MOON(800..802, R.drawable.ic_moon)
}
