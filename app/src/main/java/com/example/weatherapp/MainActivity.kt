package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.fragments.MainFragment
import com.example.weatherapp.fragments.SettingsFragment
import com.example.weatherapp.util.managers.LocationHelperManager


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        const val PREF_TEMPERATURE_UNIT = "temperature unit"
        const val PREF_TIME_FORMAT = "time format"
    }

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
            MainViewModelFactory(LocationHelperManager(this, locationPermissionRequest), repository)
        viewModel = ViewModelProvider(this, modelFactory)[MainViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        initObservers()

        with(binding) {
            for (menuItem in topAppBar.menu.children) {
                menuItem.isVisible = true
            }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.refresh -> {
                        navHostFragment.childFragmentManager
                            .fragments
                            .first()
                            .run {
                                if (this is MainFragment) {
                                    this.showProgressBar()
                                }
                            }
                        viewModel.loadData()
                        true
                    }
                    R.id.settings -> {
                        manageToolBar(false)
                        navController.navigate(R.id.action_mainFragment_to_settingsFragment)
                        topAppBar.setNavigationOnClickListener {
                            manageToolBar(true)
                            navController.navigate(R.id.action_settingsFragment_to_mainFragment)
                        }
                        true
                    }
                    else -> false
                }
            }
        }


    }

    private fun displayMenuItems(state: Boolean) {
        binding.topAppBar.menu.children.forEach { item ->
            item.isVisible = state
        }
    }

    private fun manageToolBar(isFromSetting: Boolean) {
        displayMenuItems(isFromSetting)
        binding.topAppBar.run {
            navigationIcon = AppCompatResources.getDrawable(
                this@MainActivity,
                if (isFromSetting) R.drawable.ic_baseline_menu_24 else R.drawable.ic_baseline_arrow_back_24
            )
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
                    it.city.run {
                        binding.topAppBar.title =
                            getString(R.string.city_country_format, name, country)
                    }
                },
                onFailure =
                {
                    Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    override fun onBackPressed() {
        val fragmentContainer = this.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (fragmentContainer is NavHostFragment) {
            fragmentContainer.childFragmentManager.fragments.first()
                .takeIf { it is SettingsFragment }?.let {
                    manageToolBar(true)
                }
        }
        super.onBackPressed()
    }


    private fun checkLocation(){
        val isAuto = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREF_OBTAINING_LOCATION, true)
        // if auto -> MainViewModel.loadData()
        // if manually -> asking city -> getting weather from city, save city somewhere
    }
}



