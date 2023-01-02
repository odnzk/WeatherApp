package com.example.weatherapp.app.presentation.util

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.domain.exceptions.LocationPermissionDeniedException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class LocationPermissionManager(private val application : Application) {

    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    fun getLocation(): Result<Task<Location>> {
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure(LocationPermissionDeniedException())
        }
        return Result.success(fusedLocationProviderClient.lastLocation)
    }
}
