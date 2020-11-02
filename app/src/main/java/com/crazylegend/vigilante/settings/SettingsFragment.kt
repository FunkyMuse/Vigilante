package com.crazylegend.vigilante.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.crazylegend.kotlinextensions.context.packageVersionName
import com.crazylegend.kotlinextensions.preferences.booleanChangeListener
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.providers.PrefsProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var prefsProvider: PrefsProvider

    private var notificationsSwitch: SwitchPreferenceCompat? = null
    private var version: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        notificationsSwitch = findPreference(NOTIFICATIONS_PREF_KEY)
        version = findPreference(VERSION_PREF_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        version?.summary = requireContext().packageVersionName
        notificationsSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.updateNotificationsValue(newValue)
            updateNotificationSwitch()
        }
    }

    override fun onResume() {
        super.onResume()
        updateNotificationSwitch()
    }

    private fun updateNotificationSwitch() {
        notificationsSwitch?.isChecked = prefsProvider.notificationsStatus
    }
}