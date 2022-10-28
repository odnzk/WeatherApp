package com.example.weatherapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.response.MainInformationAboutDay
import com.example.weatherapp.databinding.RvitemWeatherBinding
import com.example.weatherapp.extensions.setWeatherIcon
import com.example.weatherapp.util.managers.ConvertingManager

class WeatherHolder(private val binding: RvitemWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val converter = ConvertingManager(binding.root.resources)

    fun bind(item: MainInformationAboutDay, temperatureUnit: String, timeFormat: String) {
        val res = itemView.context.resources
        with(binding) {
            item.run {
                main.run {
                    tvTemperature.text = res.getString(
                        R.string.temperature_unit,
                        converter.convertTemp(temperatureUnit, temp),
                        temperatureUnit
                    )
                    tvTitleHumidity.text = res.getString(R.string.humidity_unit, humidity)
                }
                dt.run {
                    tvTime.text = converter.convertTimeToString(timeFormat, this)
                    tvDate.text = converter.convertDateToString(this)
                }
                ivWeather.setWeatherIcon(item.weather[0].id, item.weather[0].icon)
            }
        }
    }


    companion object {
        fun create(parent: ViewGroup): WeatherHolder =
            WeatherHolder(
                RvitemWeatherBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
    }
}
