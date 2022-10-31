package com.example.weatherapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.MainViewModel
import com.example.weatherapp.viewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            repository,
            PreferenceManager.getDefaultSharedPreferences(this),
            application
        )
    }

    @Inject
    lateinit var repository: WeatherRepository

//    private val locationPermissionRequest by lazy(::initLocationPermissionRequest)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        val locationPermissionRequest = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
//                    // Precise location access granted.
//                    Log.d("TAGTAG", "permissions granted")
//                }
//                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
//                    // Only approximate location access granted.
//                    Log.d("TAGTAG", "")
//                }
//                else -> {
//                    // No location access granted.
//                    Log.d("TAGTAG", "")
//                }
//            }
//        }
        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.


        setUpNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.mainFragment),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(
            navController = navController,
            configuration = appBarConfiguration
        )
    }

//    private fun initLocationPermissionRequest(): ActivityResultLauncher<Array<String>> {
//        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            when {
//                permissions.getOrDefault(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    false
//                ) -> {
//                    // Precise location access granted.
//                    Toast.makeText(
//                        this,
//                        getString(R.string.precise_location_access_granted),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                permissions.getOrDefault(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    false
//                ) -> {
//                    // Only approximate location access granted.
//                    Toast.makeText(
//                        this,
//                        getString(R.string.only_approximate_location_access_granted),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                else -> {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.no_location_access_granted),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> viewModel.loadData()
            R.id.settingsFragment ->
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_settingsFragment)
            android.R.id.home ->
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_settingsFragment_to_mainFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val PREF_NAME = "weather preferences"
        const val PREF_TEMPERATURE_UNIT_KEY = "temperature unit"
        const val PREF_TIME_FORMAT_KEY = "time format"
        const val PREF_CITY_KEY = "location city"
        const val PREF_COUNTRY_KEY = "location country"
        const val PREF_IS_AUTO = "is auto"
        const val DEFAULT_TEMPERATURE_UNIT = "K"
        const val DEFAULT_TIMEFORMAT = "EE, HH:mm"
    }
}



