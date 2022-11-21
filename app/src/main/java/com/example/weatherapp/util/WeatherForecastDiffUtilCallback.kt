package com.example.weatherapp.util

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.example.weatherapp.data.response.MainInformationAboutDay

class WeatherForecastDiffUtilCallback : ItemCallback<MainInformationAboutDay>() {
    override fun areItemsTheSame(
        oldItem: MainInformationAboutDay,
        newItem: MainInformationAboutDay
    ): Boolean = oldItem.dt == newItem.dt

    override fun areContentsTheSame(
        oldItem: MainInformationAboutDay,
        newItem: MainInformationAboutDay
    ): Boolean = oldItem == newItem
}
