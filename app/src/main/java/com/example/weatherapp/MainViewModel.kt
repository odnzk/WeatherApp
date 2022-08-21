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
    private val repository: WeatherRepository
) : AndroidViewModel(application) {

    private val _weatherForecast = MutableLiveData<Result<WeatherForecast>>()
    val weatherForecast: LiveData<Result<WeatherForecast>> = _weatherForecast
    private val locationManager: LocationManager = LocationManager(
        application,
        locationPermissionRequest
    )

    // Load data from a suspend fun and mutate state
    init {
        loadData()
    }

    fun loadData() {
        locationManager.getLastKnownLocation()?.let {
            it.addOnSuccessListener { location ->
                viewModelScope.launch {
                    val result = repository.getWeatherForecast(
                        location.latitude.toInt(),
                        location.longitude.toInt()
                    )
                    _weatherForecast.value = Result.success(result)
                }
            }
        }
    }
}
