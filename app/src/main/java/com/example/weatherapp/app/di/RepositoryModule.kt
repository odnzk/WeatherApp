package com.example.weatherapp.app.di

import com.example.data.repository.WeatherRepositoryImpl
import com.example.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun providesRepository(impl: WeatherRepositoryImpl): WeatherRepository
}
