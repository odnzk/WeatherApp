package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    getLastKnownLocation()
                }
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    getLastKnownLocation()
                } else -> {
                    Toast.makeText(this, "Allow using location  to get Weather data", Toast.LENGTH_SHORT).show()
                // No location access granted.
            }
            }
        }


        with(binding) {
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.refresh -> {
                        getLastKnownLocation()
                        true
                    }
                    R.id.settings -> {
                        // write navGraph
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun arePermissionsAllowed(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                return false
            }
        }
        return true
    }

    private fun askForPermission(vararg permissions: String){
        locationPermissionRequest.launch(permissions as Array<String>?)
    }


    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        if (!arePermissionsAllowed(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            askForPermission(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            // all permissions is granted
            log("we have all permissions horay!")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        getWeatherData(location.longitude, location.altitude)
                    }
                }
        }
    }


    private fun getWeatherData(longitude: Double, altitude: Double) {
        binding.textView.text = "longtitide: $longitude altitude: $altitude"
    }

    private fun log(str: String) {
        Log.d("Help", str)
    }
}
