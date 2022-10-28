package com.example.weatherapp.extensions

import android.widget.ImageView
import com.example.weatherapp.R
import com.example.weatherapp.util.managers.ConvertingManager

fun ImageView.setWeatherIcon(id: Int, icon: String){
    val imageRes = when (id) {
        ConvertingManager.CLOUDS_WITH_SUN_ICON -> {
            if (icon.last() == ConvertingManager.NIGHT_CHAR) R.drawable.ic_moonclouds else R.drawable.ic_sunclouds
        }
        in ConvertingManager.CLOUDS_ICON_LOWER_BOUND..ConvertingManager.CLOUDS_ICON_UPPER_BOUND -> R.drawable.ic_clouds
        in ConvertingManager.RAIN_ICON_LOWER_BOUND..ConvertingManager.RAIN_ICON_UPPER_BOUND -> R.drawable.ic_rain
        in ConvertingManager.SNOW_ICON_LOWER_BOUND..ConvertingManager.SNOW_ICON_UPPER_BOUND -> R.drawable.ic_snow
        in ConvertingManager.MIST_ICON_LOWER_BOUND..ConvertingManager.MIST_ICON_UPPER_BOUND -> R.drawable.ic_mist
        else -> {
            if (icon.last() == ConvertingManager.NIGHT_CHAR) R.drawable.ic_moon else R.drawable.ic_sun
        }
    }
    this.setImageResource(imageRes)
}
