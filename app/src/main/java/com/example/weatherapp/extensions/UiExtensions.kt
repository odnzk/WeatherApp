package com.example.weatherapp.extensions

import android.widget.ImageView
import com.example.weatherapp.R
import com.example.weatherapp.util.enum.WeatherIcons

fun ImageView.setWeatherIcon(id: Int, icon: String) {
    var imageRes = R.drawable.ic_undefined
    WeatherIcons.values().toList().forEach {
        if (id in it.numberCodes && icon.last() == it.timeStateChar) {
            imageRes = it.idDrawable
        }
    }
    this.setImageResource(imageRes)
}
