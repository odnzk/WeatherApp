package com.example.weatherapp

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.util.managers.LocationPermissionManager
import kotlinx.coroutines.launch

const val PREF_OBTAINING_LOCATION = "location service"

class MainViewModel(
    private val repository: WeatherRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _weatherForecast = MutableLiveData<Result<WeatherForecast>>()
    val weatherForecast: LiveData<Result<WeatherForecast>> = _weatherForecast

    private val locationPermissionManager = LocationPermissionManager(application)


    init {
        loadData()
    }

    fun loadData() {
        val locationRes = locationPermissionManager.getLocation()
        locationRes.fold(onSuccess = { task ->
            task.addOnSuccessListener {
                if (it == null) {
                    _weatherForecast.value = Result.failure(java.lang.NullPointerException())
                } else {
                    updateWeatherForecast(it)
                }
            }
        },
            onFailure = {
                _weatherForecast.value = Result.failure(it)
            })
    }

    private fun updateWeatherForecast(it: Location) {
        viewModelScope.launch {
            val result = repository.getWeatherForecast(
                latitude = it.latitude,
                longitude = it.longitude
            )
            _weatherForecast.value = Result.success(result)
        }
    }


//    fun loadData() {
//        val preferences = PreferenceManager.getDefaultSharedPreferences(getApplication())
//        val isAuto = preferences.getString(PREF_OBTAINING_LOCATION, "true").toBoolean()
//        if (isAuto) {
//            loadDataAuto()
//        } else {
//            PreferenceManager.getDefaultSharedPreferences(getApplication())
//                .getString(MainActivity.PREF_CITY_KEY, " ")
//                ?.apply {
//                    loadDataManually(this.split(" ")[0].replace(",", "").trim())
//                }
//        }
//    }
//
//    private fun loadDataManually(cityName: String?) {
//        if (!cityName.isNullOrEmpty()){
//            viewModelScope.launch {
//                val result = repository.getWeatherForecast(
//                    cityName,
//                )
//                _weatherForecast.value = Result.success(result)
//            }
//        }
//    }

}
