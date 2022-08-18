package com.example.weatherapp.util

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.response.MainInformationAboutDay
import com.example.weatherapp.databinding.RvitemWeatherBinding

class WeatherHolder(private val binding: RvitemWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MainInformationAboutDay, res: Resources) {
        with(binding) {
            item.main.run {
                tvTemperature.text = res.getString(R.string.temperature_unit, temp)
                tvTime.text = res.getString(R.string.humidity)
                tvTitleHumidity.text = res.getString(R.string.humidity_unit, humidity)
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
