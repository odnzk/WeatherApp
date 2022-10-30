package com.example.weatherapp

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            repository,
            application
        )
    }

    @Inject
    lateinit var repository: WeatherRepository

    private val locationPermissionRequest by lazy(::initLocationPermissionRequest)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    Log.d("TAGTAG", "permissions granted")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    Log.d("TAGTAG", "")
                }
                else -> {
                    // No location access granted.
                    Log.d("TAGTAG", "")
                }
            }
        }
        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.

        setUpNavigation() // todo

    }


    private fun setUpNavigation(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.mainFragment),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        // todo
//        setSupportActionBar(binding.toolbar)
//        setupActionBarWithNavController(
//            navController = navController,
//            configuration = appBarConfiguration
//        )
//        setupActionBarWithNavController(navController)
    }

    private fun initLocationPermissionRequest(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
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
                    Manifest.permission.ACCESS_COARSE_LOCATION,
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
                }
            }
        }
    }

    //    private fun initObservers(){
//        viewModel.weatherForecast.observe(this){ resWeatherForecast ->
//            resWeatherForecast.fold(
//                onSuccess = {
//                    it.city.run {
//                        saveToPreferences(name, country)
//                    }
//                },
//                onFailure =
//                {
//                    Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
//                })
//        }
//    }
//
//    private fun saveToPreferences(city: String, country: String?) {
//        PreferenceManager.getDefaultSharedPreferences(this@MainActivity).edit().run {
//            if (country == null) {
//                putString(PREF_CITY_KEY, city)
//            } else {
//                putString(
//                    PREF_CITY_KEY,
//                    resources.getString(R.string.city_country_format, city, country)
//                )
//            }
//            apply()
//        }
//    }
//
    companion object {
        const val PREF_TEMPERATURE_UNIT_KEY = "temperature unit"
        const val PREF_TIME_FORMAT_KEY = "time format"
        const val PREF_CITY_KEY = "location city"
        const val PREF_IS_AUTO = "location service"
        const val DEFAULT_TEMPERATURE_UNIT = "K"
        const val DEFAULT_TIMEFORMAT = "EE, HH:mm"
    }
}



