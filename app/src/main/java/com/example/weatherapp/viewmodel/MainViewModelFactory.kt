package com.example.weatherapp.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.WeatherRepository

class MainViewModelFactory(
    private val repository: WeatherRepository,
    private val sp: SharedPreferences,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            repository,
            sp,
            application
        ) as T
    }
}
