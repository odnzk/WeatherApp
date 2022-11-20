package com.example.weatherapp.extensions

import android.widget.ImageView
import com.example.weatherapp.R
import com.example.weatherapp.util.enum.WeatherDayIcons
import com.example.weatherapp.util.enum.WeatherNightIcons

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
