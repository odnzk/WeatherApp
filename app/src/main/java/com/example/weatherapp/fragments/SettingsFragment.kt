package com.example.weatherapp.fragments

import android.os.Bundle
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
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.viewmodel.MainViewModel
import com.example.weatherapp.viewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: MainViewModel by lazy{
        val factory = MainViewModelFactory(
            repository,
            application = requireActivity().application,
            sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    @Inject
    lateinit var repository: WeatherRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        activity?.invalidateOptionsMenu()

        findPreference<EditTextPreference>(MainActivity.PREF_CITY_KEY)?.context?.setTheme(R.style.Theme_AlertDialog_Default)
        findPreference<EditTextPreference>(MainActivity.PREF_CITY_KEY)?.setOnPreferenceChangeListener { _, _ ->
            viewModel.loadData()
            true
        }

        findPreference<DropDownPreference>(MainActivity.PREF_IS_AUTO)?.setOnPreferenceChangeListener { _, _ ->
            viewModel.loadData()
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            setBackgroundColor(getColor(requireContext(), R.color.main_blue))
        }
    }
}

