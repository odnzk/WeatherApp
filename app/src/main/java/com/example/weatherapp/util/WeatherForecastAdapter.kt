package com.example.weatherapp.util

import android.content.res.Resources
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.response.MainInformationAboutDay

class WeatherForecastAdapter(private val list: List<MainInformationAboutDay>, private val resources: Resources) :
    RecyclerView.Adapter<WeatherHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder =
        WeatherHolder.create(parent)

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(list[position], resources)
    }

    override fun getItemCount(): Int = list.size
}


