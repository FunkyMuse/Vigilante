package com.crazylegend.vigilante.settings

import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.crazylegend.kotlinextensions.context.packageVersionName
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.preferences.booleanChangeListener
import com.crazylegend.kotlinextensions.preferences.onClick
import com.crazylegend.kotlinextensions.preferences.stringChangeListener
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.utils.HOME_PAGE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        const val SAFE_DOT_POSITION = 1
    }

    @Inject
    lateinit var prefsProvider: PrefsProvider


    private var notificationsSwitch: SwitchPreferenceCompat? = null
    private var version: Preference? = null
    private var dateFormat: ListPreference? = null
    private var dotSwitch: SwitchPreferenceCompat? = null
    private var homePage: Preference? = null
    private var excludeVigilanteFromNotificationsSwitch: SwitchPreferenceCompat? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        notificationsSwitch = findPreference(NOTIFICATIONS_PREF_KEY)
        dateFormat = findPreference(DATE_PREF_KEY)
        homePage = findPreference(HOME_PAGE_PREF)
        version = findPreference(VERSION_PREF_KEY)
        dotSwitch = findPreference(DOT_PREF_KEY)
        excludeVigilanteFromNotificationsSwitch = findPreference(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        version?.summary = requireContext().packageVersionName
        notificationsSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.updateNotificationsValue(newValue)
            updateNotificationSwitch()
        }


        homePage.onClick {
            requireContext().openWebPage(HOME_PAGE) {
                shortToast(R.string.web_browser_required)
            }
        }

        excludeVigilanteFromNotificationsSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.setExcludeVigilanteFromNotificationsStatus(newValue)
        }

        dotSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.setDotStatus(newValue)
        }

        dateFormat.stringChangeListener { _, newValue ->
            prefsProvider.updateDateFormat(newValue)
            updateDateSummary()
        }
    }


    override fun onResume() {
        super.onResume()
        updateNotificationSwitch()
        updateDateSummary()
    }

    private fun updateDateSummary() {
        dateFormat?.summary = getString(R.string.current_date_format, prefsProvider.getDateFormat)
    }

    private fun updateNotificationSwitch() {
        notificationsSwitch?.isChecked = prefsProvider.areNotificationsEnabled
    }
}