package com.example.weatherapp.app.presentation.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherapp.app.MainActivity

class WeatherForecastAdapter :
    ListAdapter<com.example.domain.model.WeatherInfo, WeatherHolder>(WeatherForecastDiffUtilCallback()) {

    var temperatureUnit: String = MainActivity.DEFAULT_TEMPERATURE_UNIT
    var timeformat: String = MainActivity.DEFAULT_TIMEFORMAT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder =
        WeatherHolder.create(parent)

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(getItem(position), temperatureUnit, timeformat)
    }
}


