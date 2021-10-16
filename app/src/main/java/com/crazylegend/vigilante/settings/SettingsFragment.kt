package com.crazylegend.vigilante.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.crazylegend.context.packageVersionName
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.fragment.viewCoroutineScope
import com.crazylegend.intent.openWebPage
import com.crazylegend.kotlinextensions.preferences.booleanChangeListener
import com.crazylegend.kotlinextensions.preferences.onClick
import com.crazylegend.kotlinextensions.preferences.preference
import com.crazylegend.kotlinextensions.preferences.stringChangeListener
import com.crazylegend.locale.LocaleHelper
import com.crazylegend.toaster.Toaster
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.providers.AuthProvider
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.utils.GITHUB_URL
import com.crazylegend.vigilante.utils.MY_OTHER_APPS_URL
import com.crazylegend.vigilante.utils.addSpacingForPreferenceBackButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var prefsProvider: DefaultPreferencessProvider

    @Inject
    lateinit var authProvider: AuthProvider

    @Inject
    lateinit var toaster: Toaster

    private val version by preference<Preference>(VERSION_PREF_KEY)
    private val dateFormat by preference<ListPreference>(DATE_PREF_KEY)
    private val homePage by preference<Preference>(HOME_PAGE_PREF)
    private val excludeVigilanteFromNotificationsSwitch by preference<SwitchPreferenceCompat>(
            EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY
    )
    private val biometricAuth by preference<SwitchPreferenceCompat>(BIOMETRIC_AUTH_PREF_KEY)
    private val language by preference<ListPreference>(LANG_PREF_KEY)
    private val deleteHistory by preference<SwitchPreferenceCompat>(DELETE_HISTORY_PREF_KEY)

    private val cameraCategory by preference<Preference>(PREF_CATEGORY_CAMERA)
    private val micCategory by preference<Preference>(PREF_CATEGORY_MIC)
    private val locationCategory by preference<Preference>(PREF_CATEGORY_LOCATION)
    private val myOtherApps by preference<Preference>(MY_OTHER_APPS_PREF_KEY)
    private val logging by preference<Preference>(LOGGING_PREF)


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSpacingForPreferenceBackButton()
        version.summary = requireContext().packageVersionName

        cameraCategory.onClick { findNavController().navigate(SettingsFragmentDirections.openCameraPrefs()) }
        micCategory.onClick { findNavController().navigate(SettingsFragmentDirections.openMicPrefs()) }
        locationCategory.onClick { findNavController().navigate(SettingsFragmentDirections.openLocationPrefs()) }
        logging.onClick { findNavController().navigate(SettingsFragmentDirections.openLoggingPrefs()) }

        deleteHistory.booleanChangeListener { _, newValue ->
            if (newValue) {
                prefsProvider.scheduleDeletionHistory()
            } else {
                prefsProvider.cancelDeletionHistory()
            }
        }

        language.stringChangeListener { _, newValue ->
            LocaleHelper.setLocale(requireContext(), newValue)
            requireActivity().recreate()
        }

        homePage.onClick {
            requireContext().openWebPage(GITHUB_URL) {
                toaster.shortToast(R.string.web_browser_required)
            }
        }

        myOtherApps.onClick {
            requireContext().openWebPage(MY_OTHER_APPS_URL) {
                toaster.shortToast(R.string.web_browser_required)
            }
        }

        excludeVigilanteFromNotificationsSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.setExcludeVigilanteFromNotificationsStatus(newValue)
        }



        dateFormat.stringChangeListener { _, newValue ->
            prefsProvider.updateDateFormat(newValue)
            updateDateSummary()
        }
    }


    override fun onResume() {
        super.onResume()
        updateDateSummary()
        updateBiometricAuthAvailability()
    }


    private fun updateBiometricAuthAvailability() {
        val summary =
            if (authProvider.canAuthenticate) getString(R.string.biometric_auth_expl) else getString(
                R.string.unsupported_device
            )
        biometricAuth.summary = summary
        biometricAuth.isEnabled = authProvider.canAuthenticate
        if (authProvider.canAuthenticate) {
            biometricAuth.isChecked = prefsProvider.isBiometricAuthEnabled
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
            biometricAuth.isChecked = status
        }
    }

    private fun updateDateSummary() {
        dateFormat.summary = getString(R.string.current_date_format, prefsProvider.getDateFormat)
    }


}