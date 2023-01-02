package com.example.weatherapp.ext

import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
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

fun Fragment.showToast(@StringRes resId: Int) {
    Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}
