package com.example.weatherapp.util

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.response.MainInformationAboutDay
import com.example.weatherapp.databinding.RvitemWeatherBinding
import com.example.weatherapp.util.managers.ConvertingManager

class WeatherHolder(private val binding: RvitemWeatherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MainInformationAboutDay, res: Resources) {
        val converter = ConvertingManager(res)
        with(binding) {
            item.run {
                main.run {
                    tvTemperature.text = res.getString(R.string.temperature_unit, temp.toInt())
                    tvTitleHumidity.text = res.getString(R.string.humidity_unit, humidity)
                }
                dt.run {
                    tvTime.text = converter.convertTime(this)
                    tvDate.text = converter.convertDate(this)
                }
                ivWeather.setImageResource(
                    converter.convertIcon(
                        item.weather[0].id,
                        item.weather[0].icon
                    )
                )
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
