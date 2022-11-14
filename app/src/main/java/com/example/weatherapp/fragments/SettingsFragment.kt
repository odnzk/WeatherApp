package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.*
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

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var repository: WeatherRepository

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        activity?.invalidateOptionsMenu()

        val factory = MainViewModelFactory(
            repository,
            application = requireActivity().application,
            sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        findPreference<EditTextPreference>(MainActivity.PREF_CITY_KEY)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.loadData()
            true
        }

        findPreference<DropDownPreference>(MainActivity.PREF_IS_AUTO)?.setOnPreferenceChangeListener { _, newValue ->
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
            setBackgroundColor(getColor(requireContext(), R.color.purple))
        }
    }
}

