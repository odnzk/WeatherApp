package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.ViewModelProvider
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.weatherapp.MainActivity
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.PREF_OBTAINING_LOCATION
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var repository: WeatherRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        viewModel =
            ViewModelProvider(requireActivity())[MainViewModel::class.java]

        findPreference<EditTextPreference>(MainActivity.PREF_CITY_KEY)?.setOnPreferenceChangeListener { _, newValue ->
            val isAuto = PreferenceManager.getDefaultSharedPreferences(requireContext()).run {
                edit().putString(MainActivity.PREF_CITY_KEY, newValue.toString()).apply()
                getString(PREF_OBTAINING_LOCATION, "true").toBoolean()
            }
            if (!isAuto) {
                viewModel.loadData()
            }
            true
        }

        findPreference<DropDownPreference>(MainActivity.PREF_IS_AUTO)?.setOnPreferenceChangeListener {
                _, newValue ->
            if(newValue.toString().toBoolean()){
                viewModel.loadData()
            }
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setBackgroundColor(getColor(requireContext(), R.color.purple))
        }
    }


}
