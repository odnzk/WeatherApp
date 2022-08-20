package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.MainViewModelFactory
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.response.WeatherForecast
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.util.WeatherForecastAdapter
import com.example.weatherapp.util.managers.ConvertingManager

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var viewModel: MainViewModel

    private val repository by lazy {
        WeatherRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        showProgressBar()


        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                when {
                    permissions.getOrDefault(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        false
                    ) -> {
                        // Precise location access granted.
                        Toast.makeText(
                            context,
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
                            context,
                            getString(R.string.only_approximate_location_access_granted),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        Toast.makeText(
                            context,
                            getString(R.string.no_location_access_granted),
                            Toast.LENGTH_SHORT
                        ).show()
                        // No location access granted.
                    }
                }
            }

        val modelFactory =
            MainViewModelFactory(activity?.application!!, locationPermissionRequest, repository)
        viewModel = ViewModelProvider(this, modelFactory)[MainViewModel::class.java]

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
        with(binding) {
            val converter = ConvertingManager(resources)
            weatherForecast.list[0].run {
                with(main) {
                    tvTemperature.text = getString(R.string.temperature_unit, temp.toInt())
                    tvPressure.text = getString(R.string.pressure_unit, pressure)
                    tvHumidity.text = getString(R.string.humidity_unit, humidity)
                }
                tvVisibility.text = converter.convertVisibility(visibility)
                tvWind.text = getString(R.string.wind_unit, converter.formatDouble(wind.speed))
                ivWeather.setImageResource(converter.convertIcon(weather[0].id, weather[0].icon))
            }
            tvDayAndTime.text = converter.convertDayTime()
            rvWeather.adapter = WeatherForecastAdapter(weatherForecast.list.drop(1), resources)
        }
    }
}
