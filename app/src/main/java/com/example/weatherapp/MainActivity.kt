package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>


//    private val repository by lazy {
//        WeatherRepository()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.refresh -> {
                        // we get weather forecast from MainActivity and
                        // send it to the ViewModel, to which later Fragment
                        // will subscribe
//                        val locationManager = LocationManager(
//                            this@MainActivity,
//                            locationPermissionRequest
//                        )
//                        locationManager.getLastKnownLocation()?.let {
//                            it.addOnSuccessListener { location ->
//                                lifecycleScope.launch {
//                                    try {
//                                        val weather = repository.getWeatherForecast(
//                                            location.longitude.toInt(),
//                                            location.latitude.toInt()
//                                        )
//                                        // sending weather forecast to the ViewModel
//                                    } catch (ex: Exception) {
//                                        Log.e("", ex.message.toString())
//                                    }
//                                }
//                            }
                        // }
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
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    // Precise location access granted.
                    Toast.makeText(
                        this,
                        getString(R.string.precise_location_access_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    // Only approximate location access granted.
                    Toast.makeText(
                        this,
                        getString(R.string.only_approximate_location_access_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.no_location_access_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                    // No location access granted.
                }
            }

        }

    }
    }



