package com.example.weatherapp.util.managers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import com.example.weatherapp.data.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class LocationHelperManager(
    private val activity: Activity,
    private val locationPermissionRequest: ActivityResultLauncher<Array<String>>
) : LocationListener {
    private lateinit var locationManager: LocationManager
    private var myLocation: Location? = null

//    @SuppressLint("MissingPermission")
//    fun getLastKnownLocation(): Task<android.location.Location>? {
//        if (!PermissionManager(activity, locationPermissionRequest).arePermissionsAllowed(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        ) {
//            PermissionManager(activity, locationPermissionRequest).askForPermission(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            return null
//        } else {
//            return LocationServices.getFusedLocationProviderClient(activity).lastLocation
//                .addOnSuccessListener { l ->
//                    Location(l.longitude.toInt(), l.latitude.toInt())
//                }
//        }
//    }


    @SuppressLint("MissingPermission", "ServiceCast")
    fun getLastKnownLocation(): Task<android.location.Location>? {
        if (!PermissionManager(activity, locationPermissionRequest).arePermissionsAllowed(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            PermissionManager(activity, locationPermissionRequest).askForPermission(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            return null
        } else {
            return LocationServices.getFusedLocationProviderClient(activity).lastLocation.addOnSuccessListener { l ->
                if (l == null) {
                    locationManager =
                        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val criteria = Criteria()
                    val provider = locationManager.getBestProvider(criteria, true).toString()
                    locationManager.requestLocationUpdates(provider, 1000, 0f, this)
                } else {
                    myLocation = Location(l.longitude.toInt(), l.latitude.toInt())
                }
            }
        }
    }

    override fun onLocationChanged(location: android.location.Location) {
        locationManager.removeUpdates(this)
        myLocation = Location(location.longitude.toInt(), location.latitude.toInt())
    }
}
