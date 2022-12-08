package com.example.weatherapp.app.presentation.utils.rv

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.domain.model.WeatherInfo

class WeatherForecastDiffUtilCallback : ItemCallback<com.example.domain.model.WeatherInfo>() {
    override fun areItemsTheSame(
        oldItem: com.example.domain.model.WeatherInfo,
        newItem: com.example.domain.model.WeatherInfo
    ): Boolean = oldItem.dt == newItem.dt

    override fun areContentsTheSame(
        oldItem: com.example.domain.model.WeatherInfo,
        newItem: com.example.domain.model.WeatherInfo
    ): Boolean = oldItem == newItem
}
