package com.example.weatherapp.util

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.response.MainInformationAboutDay

class WeatherForecastAdapter : RecyclerView.Adapter<WeatherHolder>() {

    private val list: MutableList<MainInformationAboutDay> = mutableListOf()
    var temperatureUnit: String = MainActivity.DEFAULT_TEMPERATURE_UNIT
    var timeformat: String = MainActivity.DEFAULT_TIMEFORMAT

    fun setData(newList: List<MainInformationAboutDay>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder =
        WeatherHolder.create(parent)

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(list[position], temperatureUnit, timeformat)
    }

    override fun getItemCount(): Int = list.size
}


