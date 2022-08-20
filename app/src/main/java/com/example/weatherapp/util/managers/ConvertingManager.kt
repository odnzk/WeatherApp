package com.example.weatherapp.util.managers

import android.content.res.Resources
import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*

class ConvertingManager(private val res: Resources) {

    companion object {
        const val M_IN_KM = 1000.0
        const val FORMAT_DOUBLE = "%.1f"
        const val FORMAT_DATE = "dd.MM"
        const val FORMAT_TIME = "HH:mm"
        const val NIGHT_CHAR = 'n'
        const val UNIX_NUMBER = 1000
        const val RAIN_ICON_LOWER_BOUND = 200
        const val RAIN_ICON_UPPER_BOUND = 531
        const val SNOW_ICON_LOWER_BOUND = 600
        const val SNOW_ICON_UPPER_BOUND = 622
        const val MIST_ICON_LOWER_BOUND = 700
        const val MIST_ICON_UPPER_BOUND = 781
        const val CLOUDS_WITH_SUN_ICON = 801
        const val CLOUDS_ICON_LOWER_BOUND = 802
        const val CLOUDS_ICON_UPPER_BOUND = 804
    }

    fun convertVisibility(visibility: Int): String {
        if (visibility < M_IN_KM) {
            return res.getString(R.string.visibility_unit_m, visibility)
        }
        return res.getString(R.string.visibility_unit_km, formatDouble(visibility / M_IN_KM))
    }

    fun formatDouble(d: Double): String {
        return String.format(FORMAT_DOUBLE, d)
    }

    fun convertIcon(id: Int, icon: String): Int {
        return when (id) {
            CLOUDS_WITH_SUN_ICON -> {
                if (icon.last() == NIGHT_CHAR) R.drawable.ic_moonclouds else R.drawable.ic_sunclouds
            }
            in CLOUDS_ICON_LOWER_BOUND..CLOUDS_ICON_UPPER_BOUND -> R.drawable.ic_clouds
            in RAIN_ICON_LOWER_BOUND..RAIN_ICON_UPPER_BOUND -> R.drawable.ic_rain
            in SNOW_ICON_LOWER_BOUND..SNOW_ICON_UPPER_BOUND -> R.drawable.ic_snow
            in MIST_ICON_LOWER_BOUND..MIST_ICON_UPPER_BOUND -> R.drawable.ic_mist
            else -> {
                if (icon.last() == NIGHT_CHAR) R.drawable.ic_moon else R.drawable.ic_sun
            }
        }
    }

    private fun format(milliseconds: Long, pattern: String): String {
        return SimpleDateFormat(pattern).format(Date(milliseconds))
    }

    fun convertDate(unix: Long): String {
        return format(unix * UNIX_NUMBER, FORMAT_DATE)
    }

    fun convertTime(unix: Long): String {
        return format(unix * UNIX_NUMBER, FORMAT_TIME)
    }
}
