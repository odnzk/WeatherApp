package com.example.weatherapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.util.managers.LocationHelperManager
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val PREF_OBTAINING_LOCATION = "location service"

class MainViewModel(
    private val locationManager: LocationHelperManager,
    private val repository: WeatherRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _weatherForecast = MutableLiveData<Result<WeatherForecast>>()
    val weatherForecast: LiveData<Result<WeatherForecast>> = _weatherForecast

    init {
        loadData()
    }

    fun loadData() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
        val isAuto = preferences.getBoolean(PREF_OBTAINING_LOCATION, true)
        if (isAuto) {
            loadDataAuto()
        } else {
            // 1) check if there is a location in preferences
            // 2) if not  -> use auto location and somehow show message about error or invalid town
            // 3)
            loadDataManually()
        }
    }

    private fun loadDataManually(cityName: String, countryCode: String) {
        viewModelScope.launch {
            val result = repository.getWeatherForecast(
                cityName,
                countryCode
            )
            _weatherForecast.value = Result.success(result)
        }
    }

    private fun loadDataAuto() {
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
