package com.example.weatherapp.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.MainInformationAboutDay
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.exceptions.InvalidCityException
import com.example.weatherapp.exceptions.LocationPermissionDeniedException
import com.example.weatherapp.extensions.setWeatherIcon
import com.example.weatherapp.util.WeatherForecastAdapter
import com.example.weatherapp.util.managers.ConvertingManager
import com.example.weatherapp.viewmodel.MainViewModel
import com.example.weatherapp.viewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        val factory = MainViewModelFactory(
            repository,
            application = requireActivity().application,
            sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private val adapter = WeatherForecastAdapter()

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true || it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                viewModel.loadData()
            }
        }

    @Inject
    lateinit var repository: WeatherRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        requestLocationPermissions()
        showProgressBar()
        initObserves()

        return binding.root
    }

    private fun initObserves() {
        viewModel.weatherForecast.observe(viewLifecycleOwner) { resWeatherForecast ->
            resWeatherForecast.fold(
                onSuccess = {
                    hideProgressBar()
                    setWeatherForecastToUi(it)
                    showActionBarTitle(it.city.name, it.city.country)
                    saveToPreferences(it.city.name, it.city.country)
                },
                onFailure =
                {
                    when (it) {
                        is LocationPermissionDeniedException -> {
                            requestLocationPermissions()
                        }
                        is InvalidCityException -> {
                            Toast.makeText(context, R.string.error_invalid_city, Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {
                            Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
        }
    }

    private fun requestLocationPermissions() {
        permissionsLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun showActionBarTitle(city: String, country: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            getString(
                R.string.city_country_format,
                city,
                country
            )
    }

    private fun showProgressBar() {
        for (view in binding.mainCl.children) {
            view.visibility = View.INVISIBLE
            if (view.id == R.id.progress_bar) {
                (view as ContentLoadingProgressBar).show()
            }
        }
    }

    private fun hideProgressBar() {
        for (view in binding.mainCl.children) {
            view.visibility = View.VISIBLE
            if (view.id == R.id.progress_bar) {
                (view as? ContentLoadingProgressBar)?.hide()
            }
        }
    }

    private fun setWeatherForecastToUi(
        weatherForecast: WeatherForecast,
    ) {
        var temperatureUnit: String
        var timeFormat: String

        PreferenceManager.getDefaultSharedPreferences(requireActivity()).run {
            temperatureUnit = getString(
                MainActivity.PREF_TEMPERATURE_UNIT_KEY,
                MainActivity.DEFAULT_TEMPERATURE_UNIT
            ).toString()
            timeFormat = getString(
                MainActivity.PREF_TIME_FORMAT_KEY,
                MainActivity.DEFAULT_TIMEFORMAT
            ).toString()
        }

        with(binding) {
            val converter = ConvertingManager(resources)
            weatherForecast.list[0].run {
                main.run {
                    tvTemperature.text = getString(
                        R.string.temperature_unit,
                        converter.convertTemp(temperatureUnit, temp),
                        temperatureUnit
                    )
//                    tvPressure.text = getString(R.string.pressure_unit, pressure)
                    tvHumidity.text = getString(R.string.humidity_unit, humidity)
                    tvFeelsLike.text =
                        getString(
                            R.string.feels_like,
                            converter.convertTemp(temperatureUnit, temp),
                            temperatureUnit
                        )
                }
                tvVisibility.text = converter.convertVisibility(visibility)
                tvWind.text = getString(R.string.wind_unit, converter.formatDouble(wind.speed))
                ivWeather.setWeatherIcon(weather[0].id, weather[0].icon)
            }
            weatherForecast.city.run {
                // EE, h:mmaa -> h:mmaa
                // EE, HH:mm -> HH:mm
                tvSunrise.text =
                    converter.convertTimeToString(timeFormat.replace("EE, ", ""), sunrise.toLong())
                tvSunset.text =
                    converter.convertTimeToString(timeFormat.replace("EE, ", ""), sunset.toLong())
            }
            textClock.format24Hour = timeFormat
            setUpAdapter(timeFormat, temperatureUnit, weatherForecast.list.drop(1))
            setUpRecycler()
        }
    }

    private fun setUpAdapter(
        timeFormat: String,
        temperatureUnit: String,
        list: List<MainInformationAboutDay>
    ) {
        adapter.submitList(list)
        adapter.timeformat = timeFormat
        adapter.temperatureUnit = temperatureUnit
    }

    private fun setUpRecycler(){
        with(binding) {
            rvWeather.addItemDecoration(
                DividerItemDecoration(
                    context,
                    RecyclerView.HORIZONTAL
                )
            )
            rvWeather.setHasFixedSize(true)
            rvWeather.adapter = adapter
        }
    }

    private fun saveToPreferences(city: String, country: String) {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .edit().run {
                putString(MainActivity.PREF_CITY_KEY, city)
                putString(MainActivity.PREF_COUNTRY_KEY, country)
                apply()
            }
    }

}
