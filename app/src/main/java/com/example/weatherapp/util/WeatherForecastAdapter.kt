package com.example.weatherapp.util

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.response.MainInformationAboutDay

class WeatherForecastAdapter :
    ListAdapter<MainInformationAboutDay, WeatherHolder>(WeatherForecastDiffUtilCallback()) {

    var temperatureUnit: String = MainActivity.DEFAULT_TEMPERATURE_UNIT
    var timeformat: String = MainActivity.DEFAULT_TIMEFORMAT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder =
        WeatherHolder.create(parent)

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(getItem(position), temperatureUnit, timeformat)
    }
}


