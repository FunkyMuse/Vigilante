package com.crazylegend.vigilante.di.providers.prefs.camera

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.crazylegend.kotlinextensions.preferences.booleanChangeListener
import com.crazylegend.kotlinextensions.preferences.onClick
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.settings.*
import com.crazylegend.vigilante.utils.addSpacingForPreferenceBackButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */

@AndroidEntryPoint
class CameraPreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var prefsProvider: CameraPrefs

    private val dotSwitch by preference<SwitchPreferenceCompat>(CAMERA_DOT_PREF_KEY)
    private val bypassDND by preference<SwitchPreferenceCompat>(CAMERA_BYPASS_DND_PREF_KEY)
    private val notificationsSwitch by preference<SwitchPreferenceCompat>(CAMERA_NOTIFICATIONS_PREF_KEY)
    private val dotAppearance by preference<Preference>(CAMERA_DOT_APPEARANCE)
    private val notificationsSound by preference<SwitchPreferenceCompat>(CAMERA_SOUNDS_PREF_KEY)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_camera, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addSpacingForPreferenceBackButton()

        dotAppearance.onClick {
            findNavController().navigate(CameraPreferenceFragmentDirections.destinationCustomization(CAMERA_CUSTOMIZATION_BASE_PREF))
        }

        notificationsSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.updateNotificationsValue(newValue)
            updateNotificationSwitch()
        }

        bypassDND.booleanChangeListener { _, newValue ->
            prefsProvider.updateDNDValue(newValue)
        }

        dotSwitch.booleanChangeListener { _, newValue ->
            prefsProvider.setDotStatus(newValue)
        }

        notificationsSound.booleanChangeListener { _, newValue ->
            prefsProvider.updateSoundValue(newValue)
        }
    }

    private fun updateDNDSummary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bypassDND.isEnabled = prefsProvider.areNotificationsEnabled
        } else {
            bypassDND.summary = getString(R.string.incompatible_os_version)
            bypassDND.isEnabled = false
        }
    }

    private fun updateNotificationSwitch() {
        notificationsSwitch.isChecked = prefsProvider.areNotificationsEnabled
        notificationsSound.isChecked = prefsProvider.isSoundEnabled
        updateDNDSummary()
    }

    override fun onResume() {
        super.onResume()
        updateDNDSummary()
        updateNotificationSwitch()
    }
}