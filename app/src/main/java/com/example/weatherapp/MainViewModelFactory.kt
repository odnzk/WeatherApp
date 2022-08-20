package com.example.weatherapp

import android.app.Application
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.WeatherRepository

class MainViewModelFactory(
    private val application: Application,
    private val locationPermissionRequest: ActivityResultLauncher<Array<String>>,
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            repository = repository,
            locationPermissionRequest = locationPermissionRequest,
            application = application
        ) as T
    }
}
