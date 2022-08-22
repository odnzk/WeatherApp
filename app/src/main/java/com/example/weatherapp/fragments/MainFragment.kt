package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.MainActivity
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.MainViewModelFactory
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.util.WeatherForecastAdapter
import com.example.weatherapp.util.managers.ConvertingManager
import com.example.weatherapp.util.managers.LocationHelperManager

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val repository by lazy {
        WeatherRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val modelFactory =
            MainViewModelFactory(
                LocationHelperManager(
                    requireActivity(),
                    MainActivity().locationPermissionRequest
                ), repository
            )
        viewModel = ViewModelProvider(this, modelFactory)[MainViewModel::class.java]

        showProgressBar()
        initObserves()

        with(binding) {
            rvWeather.addItemDecoration(
                DividerItemDecoration(
                    context,
                    RecyclerView.HORIZONTAL
                )
            )
        }

        return binding.root
    }

    private fun initObserves() {
        viewModel.weatherForecast.observe(viewLifecycleOwner) { resWeatherForecast ->
            resWeatherForecast.fold(
                onSuccess = {
                    setWeatherForecastToUi(it)
                },
                onFailure =
                {
                    Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun showProgressBar() {
        for (view in binding.mainCl.children) {
            view.visibility = View.GONE
            if (view.id == R.id.progress_bar) {
                (view as ContentLoadingProgressBar).show()
            }
        }
    }

    private fun hideProgressBar() {
        for (view in binding.mainCl.children) {
            view.visibility = View.VISIBLE
            if (view.id == R.id.progress_bar) {
                (view as ContentLoadingProgressBar).hide()
            }
        }
    }

    private fun setWeatherForecastToUi(
        weatherForecast: WeatherForecast,
    ) {
        hideProgressBar()

        val sp = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val temperatureUnit = sp.getString(MainActivity.PREF_TEMPERATURE_UNIT, "").orEmpty()
        val timeFormat = sp.getString(MainActivity.PREF_TIME_FORMAT, "").orEmpty()

        with(binding) {
            val converter = ConvertingManager(resources)
            weatherForecast.list[0].run {
                with(main) {
                    tvTemperature.text = getString(
                        R.string.temperature_unit,
                        converter.convertTemp(temperatureUnit, temp),
                        temperatureUnit
                    )
                    tvPressure.text = getString(R.string.pressure_unit, pressure)
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
                ivWeather.setImageResource(converter.convertIcon(weather[0].id, weather[0].icon))
            }
            weatherForecast.city.run {
                tvSunrise.text = converter.convertTime(timeFormat, sunrise.toLong())
                tvSunset.text = converter.convertTime(timeFormat, sunset.toLong())
            }

            textClock.format24Hour = timeFormat

            rvWeather.adapter = WeatherForecastAdapter(weatherForecast.list.drop(1), resources, sp)
        }
    }
}
