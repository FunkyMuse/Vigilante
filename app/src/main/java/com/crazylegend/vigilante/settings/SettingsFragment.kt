package com.crazylegend.vigilante.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.kotlinextensions.context.packageVersionName
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.fragments.viewCoroutineScope
import com.crazylegend.kotlinextensions.gestureNavigation.EdgeToEdge
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.locale.LocaleHelper
import com.crazylegend.kotlinextensions.preferences.booleanChangeListener
import com.crazylegend.kotlinextensions.preferences.onClick
import com.crazylegend.kotlinextensions.preferences.stringChangeListener
import com.crazylegend.kotlinextensions.views.dimen
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.contracts.EdgeToEdgeScrolling
import com.crazylegend.vigilante.di.providers.AuthProvider
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.utils.HOME_PAGE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), EdgeToEdgeScrolling {

    @Inject
    lateinit var prefsProvider: PrefsProvider

    @Inject
    lateinit var authProvider: AuthProvider

    private var notificationsSwitch: SwitchPreferenceCompat? = null
    private var version: Preference? = null
    private var dateFormat: ListPreference? = null
    private var dotSwitch: SwitchPreferenceCompat? = null
    private var homePage: Preference? = null
    private var excludeVigilanteFromNotificationsSwitch: SwitchPreferenceCompat? = null
    private var biometricAuth: SwitchPreferenceCompat? = null
    private var language: ListPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        notificationsSwitch = findPreference(NOTIFICATIONS_PREF_KEY)
        dateFormat = findPreference(DATE_PREF_KEY)
        homePage = findPreference(HOME_PAGE_PREF)
        version = findPreference(VERSION_PREF_KEY)
        dotSwitch = findPreference(DOT_PREF_KEY)
        language = findPreference(LANG_PREF_KEY)
        biometricAuth = findPreference(BIOMETRIC_AUTH_PREF_KEY)
        excludeVigilanteFromNotificationsSwitch = findPreference(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.apply {
            clipToPadding = false
            updatePadding(bottom = dimen(R.dimen.padding_bottom_scroll).toInt())
        }
        version?.summary = requireContext().packageVersionName
        notificationsSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.updateNotificationsValue(newValue)
            updateNotificationSwitch()
        }

        language.stringChangeListener { _, newValue ->
            LocaleHelper.setLocale(requireContext(), newValue)
            requireActivity().recreate()
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
        updateBiometricAuthAvailability()
    }

    private fun updateBiometricAuthAvailability() {
        val summary = if (authProvider.canAuthenticate) getString(R.string.biometric_auth_expl) else getString(R.string.unsupported_device)
        biometricAuth?.summary = summary
        biometricAuth?.isEnabled = authProvider.canAuthenticate
        if (authProvider.canAuthenticate) {
            biometricAuth?.isChecked = prefsProvider.isBiometricAuthEnabled
            biometricAuth.booleanChangeListener { _, newValue ->
                if (newValue) {
                    confirmBiometricAuth()
                } else {
                    prefsProvider.updateBiometricStatus(newValue)
                }
            }
        }
    }

    private fun confirmBiometricAuth() {
        authProvider.confirmBiometricAuth(R.string.verification_required, R.string.prompt_info_expl, onAuthFailed = {
            //auth failed action
            prefsProvider.updateBiometricStatus(false)
            updateCheckBiometricAuth()
        }, onAuthError = { _, _ ->
            //handle auth error message and codes
            prefsProvider.updateBiometricStatus(false)
            updateCheckBiometricAuth()
        }) {
            //handle successful authentication
            prefsProvider.updateBiometricStatus(true)
            updateCheckBiometricAuth(true)
        }
    }

    private fun updateCheckBiometricAuth(status: Boolean = false) {
        viewCoroutineScope.launch(mainDispatcher) {
            biometricAuth?.isChecked = status
        }
    }

    private fun updateDateSummary() {
        dateFormat?.summary = getString(R.string.current_date_format, prefsProvider.getDateFormat)
    }

    private fun updateNotificationSwitch() {
        notificationsSwitch?.isChecked = prefsProvider.areNotificationsEnabled
    }

    override fun edgeToEdgeScrollingContent() {
        EdgeToEdge.setUpScrollingContent(listView)
    }
}