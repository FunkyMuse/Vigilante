package com.crazylegend.vigilante.di.providers.prefs.logging

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.crazylegend.kotlinextensions.preferences.booleanChangeListener
import com.crazylegend.kotlinextensions.preferences.preference
import com.crazylegend.toaster.Toaster
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.settings.*
import com.crazylegend.vigilante.utils.addSpacingForPreferenceBackButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by funkymuse on 10/16/21 to long live and prosper !
 */
@AndroidEntryPoint
class LoggingPreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var loggingPrefs: LoggingPrefs

    @Inject
    lateinit var toaster: Toaster

    private val logPermissions by preference<SwitchPreferenceCompat>(LOG_PERMISSIONS_KEY)
    private val logPower by preference<SwitchPreferenceCompat>(LOG_POWER_KEY)
    private val logHeadset by preference<SwitchPreferenceCompat>(LOG_HEADSET_KEY)
    private val logNotifications by preference<SwitchPreferenceCompat>(LOG_NOTIFICATIONS_KEY)
    private val logLockScreen by preference<SwitchPreferenceCompat>(LOG_LOCKSCREEN_KEY)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_logging, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSpacingForPreferenceBackButton()
        logPermissions.booleanChangeListener { _, newValue ->
            loggingPrefs.setPermissionLoggingState(newValue)
            showRationaleMessage()
        }
        logPower.booleanChangeListener { _, newValue ->
            loggingPrefs.setPowerLoggingState(newValue)
            showRationaleMessage()
        }
        logHeadset.booleanChangeListener { _, newValue ->
            loggingPrefs.setHeadsetLoggingState(newValue)
            showRationaleMessage()
        }
        logNotifications.booleanChangeListener { _, newValue ->
            loggingPrefs.setNotificationsLoggingState(newValue)
            showRationaleMessage()
        }
        logLockScreen.booleanChangeListener { _, newValue ->
            loggingPrefs.setLockScreenLoggingState(newValue)
            showRationaleMessage()
        }
    }

    private fun showRationaleMessage() {
        toaster.shortToast(R.string.restart_service_for_changes_to_take_effect)
    }
}