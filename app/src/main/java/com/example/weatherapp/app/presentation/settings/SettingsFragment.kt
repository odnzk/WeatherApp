package com.example.weatherapp.app.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.weatherapp.R
import com.example.weatherapp.app.MainActivity
import com.example.weatherapp.app.presentation.home.HomeFragmentEvent
import com.example.weatherapp.app.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var repository: com.example.domain.repository.WeatherRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        activity?.invalidateOptionsMenu()

        findPreference<EditTextPreference>(MainActivity.PREF_CITY_KEY)?.context?.setTheme(R.style.Theme_AlertDialog_Default)
        findPreference<EditTextPreference>(MainActivity.PREF_CITY_KEY)?.setOnPreferenceChangeListener { _, _ ->
            viewModel.onEvent(HomeFragmentEvent.Reload)
            true
        }

        findPreference<DropDownPreference>(MainActivity.PREF_IS_AUTO)?.setOnPreferenceChangeListener { _, _ ->
            viewModel.onEvent(HomeFragmentEvent.Reload)
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

