package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.util.managers.LocationHelperManager

class MainViewModelFactory(
    private val locationManager: LocationHelperManager,
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            locationManager,
            repository
        ) as T
    }
}
