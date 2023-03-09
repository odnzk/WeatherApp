package com.example.weatherapp.app.presentation.home

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.model.state.State
import com.example.domain.model.weather.WeatherForecast
import com.example.domain.repository.WeatherRepository
import com.example.domain.util.InvalidCityException
import com.example.domain.util.LocationRequestFailedException
import com.example.weatherapp.app.MainActivity
import com.example.weatherapp.app.presentation.util.LocationPermissionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val sp: SharedPreferences,
    application: Application
) : AndroidViewModel(application) {

    private val _weatherForecast: MutableLiveData<State<WeatherForecast>> =
        MutableLiveData(State.Loading())
    val weatherForecast: LiveData<State<WeatherForecast>> = _weatherForecast

    private val locationPermissionManager = LocationPermissionManager(application)

    init {
        loadData()
    }

    fun onEvent(event: HomeFragmentEvent) = viewModelScope.launch {
        when (event) {
            is HomeFragmentEvent.SaveCitySettings -> {
                sp.edit().run {
                    putString(MainActivity.PREF_CITY_KEY, event.city)
                    putString(MainActivity.PREF_COUNTRY_KEY, event.country)
                    apply()
                }
            }
            HomeFragmentEvent.Reload -> loadData()
        }
    }


    private fun loadData() = viewModelScope.launch {
        if (sp.getString(MainActivity.PREF_IS_AUTO, "true").toBoolean()) {
            loadDataAuto()
        } else {
            val city: String? = sp.getString(MainActivity.PREF_CITY_KEY, "")
            if (!city.isNullOrEmpty() && city.isNotBlank()) {
                _weatherForecast.value = repository.getWeatherForecast(city)
            } else {
                _weatherForecast.value = State.Error(InvalidCityException())
            }
        }
    }

    private suspend fun loadDataAuto() = viewModelScope.launch {
        val locationRes = locationPermissionManager.getLocation()
        locationRes.fold(
            onSuccess = { task ->
                task.addOnSuccessListener {
                    if (it == null) {
                        _weatherForecast.value = State.Error(LocationRequestFailedException())
                    } else {
                        viewModelScope.launch {
                            _weatherForecast.value =
                                repository.getWeatherForecast(it.latitude, it.longitude)
                        }
                    }
                }
            },
            onFailure = {
                _weatherForecast.value = State.Error(it)
            })
    }
}
