package com.example.weatherapp

import android.app.Application
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.util.managers.LocationManager
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    locationPermissionRequest: ActivityResultLauncher<Array<String>>,
    repository: WeatherRepository
) : AndroidViewModel(application) {

    private val _weatherForecast = MutableLiveData<Result<WeatherForecast>>()
    val weatherForecast: LiveData<Result<WeatherForecast>> = _weatherForecast

    // Load data from a suspend fun and mutate state
    init {
        val locationManager = LocationManager(
            application,
            locationPermissionRequest
        )
        locationManager.getLastKnownLocation()?.let {
            it.addOnSuccessListener { location ->
                viewModelScope.launch {
                    val result = repository.getWeatherForecast(
                        location.longitude.toInt(),
                        location.latitude.toInt()
                    )
                    _weatherForecast.value = Result.success(result)
                }
            }
        }
    }
}
