package com.example.weatherapp.app.presentation.home

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.state.State
import com.example.domain.model.weather.WeatherForecast
import com.example.domain.model.weather.WeatherInfo
import com.example.domain.util.LocationPermissionDeniedException
import com.example.weatherapp.R
import com.example.weatherapp.app.MainActivity
import com.example.weatherapp.app.presentation.util.ConvertingManager
import com.example.weatherapp.app.presentation.util.extensions.*
import com.example.weatherapp.app.presentation.util.rv.WeatherForecastAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.databinding.StateLoadingBinding
import com.example.weatherapp.ext.setWeatherIcon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var _loadingBinding: StateLoadingBinding? = null
    private val loadingBinding: StateLoadingBinding get() = _loadingBinding!!
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = WeatherForecastAdapter()

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> viewModel.onEvent(HomeFragmentEvent.Reload)
                !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                    Log.d("location", "change_location_permission_in_settings")
                    binding.root.showToast(R.string.change_location_permission_in_settings)
                }
                else -> {
                    Log.d("location", "location_denied")
                    binding.root.showToast(R.string.location_denied)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserves()
    }


    private fun initObserves() {
        viewModel.weatherForecast.observe(viewLifecycleOwner) { resWeatherForecast ->
            when (resWeatherForecast) {
                is State.Loading ->{
                    binding.hideChildren()
                    loadingBinding.loadingStarted()
                }
                is State.Success -> {
                    resWeatherForecast.data?.let {
                        binding.showChildren()
                        loadingBinding.loadingFinished()
                        showWeatherForecast(it)
                        showActionBarTitle(it.city.name, it.city.country)
                        viewModel.onEvent(
                            HomeFragmentEvent.SaveCitySettings(
                                it.city.name, it.city.country
                            )
                        )
                    }
                }
                is State.Error -> resWeatherForecast.error?.let { showError(it) }
            }
        }
    }

    private fun showError(it: Throwable) {
        binding.hideChildren()
        loadingBinding.errorOccurred(it) {
            if (it is LocationPermissionDeniedException) {
                requestLocationPermissions()
            } else {
                viewModel.onEvent(HomeFragmentEvent.Reload)
            }
        }
    }

    private fun requestLocationPermissions() {
        permissionsLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun showActionBarTitle(city: String, country: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            getString(
                R.string.city_country_format,
                city,
                country
            )
    }

    private fun showWeatherForecast(
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
                    tvVisibility.text = converter.convertVisibility(visibility)
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
        list: List<WeatherInfo>
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        _loadingBinding = StateLoadingBinding.bind(binding.root)

        requestLocationPermissions()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _loadingBinding = null
        _binding = null
    }
}
