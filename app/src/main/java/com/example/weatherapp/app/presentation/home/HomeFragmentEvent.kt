package com.example.weatherapp.app.presentation.home

sealed interface HomeFragmentEvent {
    class SaveCitySettings(val city: String, val country: String) : HomeFragmentEvent
    object Reload : HomeFragmentEvent
}
