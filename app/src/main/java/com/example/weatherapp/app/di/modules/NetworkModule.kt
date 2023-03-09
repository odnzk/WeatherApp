package com.example.weatherapp.app.di.modules

import com.example.data.api.OpenWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesApi(): OpenWeatherApi = OpenWeatherApi()
}
