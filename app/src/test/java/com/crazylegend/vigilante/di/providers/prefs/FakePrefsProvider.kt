package com.crazylegend.vigilante.di.providers.prefs

import com.crazylegend.vigilante.di.providers.prefs.camera.CameraPreferences
import com.crazylegend.vigilante.di.providers.prefs.contracts.DotContract
import com.crazylegend.vigilante.di.providers.prefs.contracts.NotificationsContract
import com.crazylegend.vigilante.di.providers.prefs.customization.CustomizationPrefs.Companion.DEFAULT_DOT_COLOR
import com.crazylegend.vigilante.di.providers.prefs.customization.CustomizationPrefs.Companion.DEFAULT_DOT_SIZE
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferences
import com.crazylegend.vigilante.di.providers.prefs.mic.MicrophonePreferences

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
class FakePrefsProvider : DefaultPreferences, DotContract, NotificationsContract, CameraPreferences,
        MicrophonePreferences {

    override fun scheduleDeletionHistory() {}

    override fun cancelDeletionHistory() {}

    override fun updateDateFormat(value: String) {
        getDateFormat = value
    }

    override var getDateFormat: String = "dd.MM.yyyy HH:mm:ss"
    override var areNotificationsEnabled: Boolean = false

    override fun updateNotificationsValue(value: Boolean) {
        areNotificationsEnabled = value
    }

    override var isDarkThemeEnabled: Boolean = false

    override fun changeTheme() {
        isDarkThemeEnabled = !isDarkThemeEnabled
    }

    override var isDotEnabled: Boolean = false

    override fun setDotStatus(status: Boolean) {
        isDotEnabled = status
    }

    override var isVigilanteExcludedFromNotifications: Boolean = false

    override fun setExcludeVigilanteFromNotificationsStatus(newValue: Boolean) {
        isVigilanteExcludedFromNotifications = newValue
    }

    fun setCameraColor(color: Int) {
        getCameraColorPref = color
    }

    fun setCameraSize(size: Float) {
        getCameraSizePref = size
    }

    override var getCameraColorPref: Int = DEFAULT_DOT_COLOR
    override var getCameraSizePref: Float = DEFAULT_DOT_SIZE
    override var getCameraPositionPref: Int = 0
    override var getLayoutCameraPositionPref: Int = 0
    override var getCameraNotificationLEDColorPref: Int = 0
    override var getCameraSpacing: Int = 0
    override var getCameraVibrationPositionPref: Int = 0
    override var getCameraVibrationEffectPref: LongArray? = null
    override var getMicColorPref: Int = 0
    override var getMicSizePref: Float = 0f
    override var getMicPositionPref: Int = 0
    override var getLayoutMicPositionPref: Int = 0
    override var getMicNotificationLEDColorPref: Int = 0
    override var getMicSpacing: Int = 0
    override var getMicVibrationPositionPref: Int = 0
    override var getMicVibrationEffectPref: LongArray? = null
    override var isIntroShown: Boolean = false

    override fun setIntroShown() {
        isIntroShown = true
    }

    override var isBiometricAuthEnabled: Boolean = false

    override fun updateBiometricStatus(status: Boolean) {
        isBiometricAuthEnabled = status
    }

    override var isBypassDNDEnabled: Boolean = false

    override fun updateDNDValue(value: Boolean) {
        isBypassDNDEnabled = value
    }

}