package com.example.weatherapp.app.presentation.util.rv

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.domain.model.weather.WeatherInfo

class WeatherForecastDiffUtilCallback : ItemCallback<WeatherInfo>() {
    override fun areItemsTheSame(
        oldItem: WeatherInfo,
        newItem: WeatherInfo
    ): Boolean = oldItem.dt == newItem.dt

    override fun areContentsTheSame(
        oldItem: WeatherInfo,
        newItem: WeatherInfo
    ): Boolean = oldItem == newItem
}
