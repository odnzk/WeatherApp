package com.example.weatherapp.ext

import android.widget.ImageView
import com.example.weatherapp.R
import com.example.weatherapp.app.presentation.util.icons.WeatherDayIcons
import com.example.weatherapp.app.presentation.util.icons.WeatherNightIcons

fun ImageView.setWeatherIcon(id: Int, icon: String) {
    var imageRes = R.drawable.ic_undefined
    WeatherDayIcons.values().forEach {
        if (id in it.numberCodes) imageRes = it.idDrawable
    }
    if (icon.last() == 'n') {
        WeatherNightIcons.values().forEach {
            if (id in it.numberCodes) imageRes = it.idDrawable
        }
    }
    this.setImageResource(imageRes)
}
