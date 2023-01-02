package com.example.weatherapp.app.presentation.util

import android.content.res.Resources
import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*

class ConvertingManager(private val res: Resources) {
    companion object {
        private const val M_IN_KM = 1000.0
        private const val FORMAT_DOUBLE = "%.1f"
        private const val FORMAT_DATE = "dd.MM"
        private const val KELVIN_CONSTANT = 273.15
        private const val FAHRENHEIT_MULTIPLIER = 9 / 5
        private const val FAHRENHEIT_SUMMAND = 32
        private const val UNIX_NUMBER = 1000
    }

    fun convertVisibility(visibility: Int): String {
        if (visibility < M_IN_KM) {
            return res.getString(R.string.visibility_unit_m, visibility)
        }
        return res.getString(R.string.visibility_unit_km, formatDouble(visibility / M_IN_KM))
    }

    fun formatDouble(d: Double) = String.format(FORMAT_DOUBLE, d)

    private fun format(milliseconds: Long, pattern: String) =
        SimpleDateFormat(pattern, Locale.getDefault()).format(Date(milliseconds))

    fun convertDateToString(unix: Long): String = format(unix * UNIX_NUMBER, FORMAT_DATE)

    fun convertTimeToString(timeFormat: String, unix: Long): String =
        format(unix * UNIX_NUMBER, timeFormat)

    fun convertTemp(temperatureUnit: String, temp: Double): Int {
        return when (temperatureUnit) {
            "K" -> temp
            "F" -> convertToFahrenheit(temp)
            else -> temp - KELVIN_CONSTANT
        }.toInt()
    }

    private fun convertToFahrenheit(temp: Double): Double {
        return (temp - KELVIN_CONSTANT) * FAHRENHEIT_MULTIPLIER + FAHRENHEIT_SUMMAND
    }
}

