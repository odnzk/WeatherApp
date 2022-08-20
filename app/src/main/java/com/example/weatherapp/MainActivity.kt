package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.util.managers.LocationManager


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    val locationPermissionRequest by lazy {
        initLocationPermissionRequest()
    }

    private val repository by lazy {
        WeatherRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val modelFactory =
            MainViewModelFactory(LocationManager(this, locationPermissionRequest), repository)
        viewModel = ViewModelProvider(this, modelFactory)[MainViewModel::class.java]

        initObservers()

        with(binding) {
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.refresh -> {
                        viewModel.loadData()
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

    private fun initLocationPermissionRequest(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
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

    private fun initObservers(){
        viewModel.weatherForecast.observe(this){ resWeatherForecast ->
            resWeatherForecast.fold(
                onSuccess = {
                    it.city.run{
                        binding.topAppBar.title = getString(R.string.city_country_format, name, country)
                    }
                },
                onFailure =
                {
                    Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
                })
        }
    }
}



