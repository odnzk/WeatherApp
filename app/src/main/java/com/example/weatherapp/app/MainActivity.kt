package com.example.weatherapp.app

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.weatherapp.R
import com.example.weatherapp.app.presentation.fragments.SettingsFragment
import com.example.weatherapp.app.presentation.fragments.home.HomeViewModel
import com.example.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var repository: com.example.domain.repository.WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).run{
            val currFragment = childFragmentManager.fragments.last()
            if(currFragment != null && currFragment is SettingsFragment){
                menu?.clear()
            }
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                Log.d("TAGTAG", "reloading?")
                viewModel.loadData()
            }
            R.id.settingsFragment ->
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_mainFragment_to_settingsFragment)
            android.R.id.home ->
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_settingsFragment_to_mainFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val PREF_TEMPERATURE_UNIT_KEY = "temperature unit"
        const val PREF_TIME_FORMAT_KEY = "time format"
        const val PREF_CITY_KEY = "location city"
        const val PREF_COUNTRY_KEY = "location country"
        const val PREF_IS_AUTO = "is auto"
        const val DEFAULT_TEMPERATURE_UNIT = "K"
        const val DEFAULT_TIMEFORMAT = "EE, HH:mm"
    }
}



