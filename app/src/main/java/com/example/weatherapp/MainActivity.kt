package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.util.WeatherForecastAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>


    companion object {
        const val API_KEY = "7772c8e5d929dc484f8137d8cf85e7fb"
        const val FILE_NAME_FOR_SAVING = "weatherforecast.txt"
        const val RAIN_ICON_LOWER_BOUND = 200
        const val RAIN_ICON_UPPER_BOUND = 531
        const val SNOW_ICON_LOWER_BOUND = 600
        const val SNOW_ICON_UPPER_BOUND = 622
        const val MIST_ICON_LOWER_BOUND = 700
        const val MIST_ICON_UPPER_BOUND = 781
        const val CLOUDS_WITH_SUN_ICON = 801
        const val CLOUDS_ICON_LOWER_BOUND = 802
        const val CLOUDS_ICON_UPPER_BOUND = 804
    }

    private val repository by lazy {
        WeatherRepository()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readSavedData()?.let{
            setWeatherForecastToUi(it)
        }

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
            rvWeather.addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    RecyclerView.HORIZONTAL
                )
            )

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
            showProgressBar()
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        getWeatherData(location.longitude.toInt(), location.latitude.toInt())
                    }
                }
        }
    }


    private fun getWeatherData(longitude: Int, latitude: Int) {
        lifecycleScope.launch {
            try {
                val weatherForecast = repository.getWeatherForecast(latitude, longitude, API_KEY)
                runOnUiThread {
                    hideProgressBar()
                    setWeatherForecastToUi(weatherForecast)
                }
                saveData(weatherForecast)
            } catch (ex: Exception) {
                Log.e("", ex.message.toString())
            }

        }
    }

    private fun setWeatherForecastToUi(weatherForecast: WeatherForecast) {
        with(binding) {
            topAppBar.title = weatherForecast.city.run { "$name, $country" }
            weatherForecast.list[0].run {
                with(main) {
                    tvTemperature.text = getString(R.string.temperature_unit, temp)
                    tvPressure.text =  getString(R.string.pressure_unit, pressure)
//                    tvHumidity.text = getString(R.string.humidity_unit, humidity)
                }
                tvVisibility.text = getString(R.string.visibility_unit, visibility)
                tvWind.text = getString(R.string.wind_unit, wind.speed)
                setIconToImageView(ivWeather, weather[0].id)
            }
            tvDayAndTime.text = getDayAndTime(resources)
            rvWeather.adapter = WeatherForecastAdapter(weatherForecast.list.drop(1), resources)
        }


    }

    private fun getDayAndTime(resources: Resources): String {
        return Calendar.getInstance().run {
            convertToDayOfWeek(
                resources,
                get(Calendar.DAY_OF_WEEK)
            ) + ", " + get(Calendar.HOUR_OF_DAY) + ":" + get(
                Calendar.MINUTE
            )
        }
    }

    private fun convertToDayOfWeek(resources: Resources, day: Int): String {
        return resources.getStringArray(R.array.days_of_week)[day - 2]
    }

    private fun setIconToImageView(iv: ImageView, id: Int) {
        val iconId: Int = when (id) {
            CLOUDS_WITH_SUN_ICON -> R.drawable.ic_sunclouds
            in CLOUDS_ICON_LOWER_BOUND..CLOUDS_ICON_UPPER_BOUND -> R.drawable.ic_clouds
            in RAIN_ICON_LOWER_BOUND..RAIN_ICON_UPPER_BOUND -> R.drawable.ic_rain
            in SNOW_ICON_LOWER_BOUND..SNOW_ICON_UPPER_BOUND -> R.drawable.ic_snow
            in MIST_ICON_LOWER_BOUND..MIST_ICON_UPPER_BOUND -> R.drawable.ic_mist
            else -> R.drawable.ic_sun
        }
        iv.setImageResource(iconId)
    }

    private fun showProgressBar() {
        for (view in binding.mainCl.children) {
            view.visibility = View.GONE
        }
        binding.progressBar.show()
    }

    private fun hideProgressBar() {
        for (view in binding.mainCl.children) {
            view.visibility = View.VISIBLE
        }
        binding.progressBar.hide()
    }

    private fun saveData(weatherForecast: WeatherForecast) {
        // save to some docs
//        filesDir
//        File dir = new File(mcoContext.getFilesDir(), "mydir");
//        if(!dir.exists()){
//            dir.mkdir()
//        }
        val writer = ObjectOutputStream(FileOutputStream(File(filesDir, FILE_NAME_FOR_SAVING)))
        writer.use {
            writer.writeObject(weatherForecast)
        }
    }

    private fun readSavedData(): WeatherForecast? {
        return ObjectInputStream(FileInputStream(FILE_NAME_FOR_SAVING)).use {
            readSavedData()
        }
    }
}


