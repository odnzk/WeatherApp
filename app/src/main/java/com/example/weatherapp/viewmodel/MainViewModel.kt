package com.example.weatherapp.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.exceptions.InvalidCityException
import com.example.weatherapp.util.managers.LocationPermissionManager
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val sp: SharedPreferences,
    application: Application
) : AndroidViewModel(application) {

    private val _weatherForecast = MutableLiveData<Result<WeatherForecast>>()
    val weatherForecast: LiveData<Result<WeatherForecast>> = _weatherForecast

    private val locationPermissionManager = LocationPermissionManager(application)


    init {
        loadData()
    }

    fun loadData() {
        if (sp.getString(MainActivity.PREF_IS_AUTO, "true").toBoolean()) {
            loadDataAuto()
        } else {
            // check if pref contains correct city -> if not set failure
            val city: String? = sp.getString(MainActivity.PREF_CITY_KEY, "")

            if (!city.isNullOrEmpty() && city.isNotBlank()) {
                loadDataManually(city)
            } else {
                _weatherForecast.value = Result.failure(InvalidCityException())
            }
        }
    }

    private fun loadDataManually(cityName: String) {
        viewModelScope.launch {
            val result = repository.getWeatherForecast(
                cityName
            )
            _weatherForecast.value = Result.success(result)
        }
    }

    private fun loadDataAuto() {
        val locationRes = locationPermissionManager.getLocation()
        locationRes.fold(
            onSuccess = { task ->
                task.addOnSuccessListener {
                    if (it == null) {
                        _weatherForecast.value = Result.failure(java.lang.NullPointerException())
                    } else {
                        updateWeatherForecastAuto(it)
                    }
                }
            },
            onFailure = {
                _weatherForecast.value = Result.failure(it)
            })
    }

    private fun updateWeatherForecastAuto(it: Location) {
        viewModelScope.launch {
            val result = repository.getWeatherForecast(
                latitude = it.latitude,
                longitude = it.longitude
            )
            _weatherForecast.value = Result.success(result)
        }
    }
}
