package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.util.managers.LocationHelperManager
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainViewModel(
    private val locationManager: LocationHelperManager,
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherForecast = MutableLiveData<Result<WeatherForecast>>()
    val weatherForecast: LiveData<Result<WeatherForecast>> = _weatherForecast

    init {
        loadData()
    }

    fun loadData() {
        locationManager.getLastKnownLocation()?.let {
            it.addOnSuccessListener { location ->
                viewModelScope.launch {
                    val result = repository.getWeatherForecast(
                        location.latitude.roundToInt(),
                        location.longitude.roundToInt()
                    )
                    _weatherForecast.value = Result.success(result)
                }
            }
        }
    }
}
