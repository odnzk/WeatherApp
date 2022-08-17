package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("weather?lat={lat}&lon={lon}&appid=7772c8e5d929dc484f8137d8cf85e7fb")
    suspend fun getWeather(@Path("lat") latitude: Double, @Path("lon") longitude: Double)

}
