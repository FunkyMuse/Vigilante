package com.crazylegend.vigilante.di.providers.prefs.logging

import android.content.SharedPreferences
import com.crazylegend.sharedpreferences.putBoolean
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by funkymuse on 10/16/21 to long live and prosper !
 */
@Singleton
class LoggingPrefs @Inject constructor(@EncryptedPrefs private val defaultPrefs: SharedPreferences) : LoggingPreferences {

    override val isPermissionLoggingEnabled: Boolean
        get() = getBooleanState(LOG_PERMISSIONS_KEY, true)

    override fun setPermissionLoggingState(isEnabled: Boolean) = putBooleanState(LOG_NOTIFICATIONS_KEY, isEnabled)

    override val isPowerLoggingEnabled: Boolean
        get() = getBooleanState(LOG_POWER_KEY, true)

    override fun setPowerLoggingState(isEnabled: Boolean) = putBooleanState(LOG_POWER_KEY, isEnabled)

    override val isHeadsetLoggingEnabled: Boolean
        get() = getBooleanState(LOG_HEADSET_KEY, true)

    override fun setHeadsetLoggingState(isEnabled: Boolean) = putBooleanState(LOG_HEADSET_KEY, isEnabled)

    override val isNotificationsLoggingEnabled: Boolean
        get() = getBooleanState(LOG_NOTIFICATIONS_KEY, true)

    override val isEmptyNotificationsLoggingEnabled: Boolean
        get() = getBooleanState(LOG_EMPTY_PERMISSIONS_KEY, true)

    override fun setNotificationsLoggingState(isEnabled: Boolean) = putBooleanState(LOG_NOTIFICATIONS_KEY, isEnabled)

    override val isLockScreenLoggingEnabled: Boolean
        get() = getBooleanState(LOG_LOCKSCREEN_KEY, true)

    override fun setLockScreenLoggingState(isEnabled: Boolean) = putBooleanState(LOG_LOCKSCREEN_KEY, isEnabled)

    private fun putBooleanState(key: String, state: Boolean) = defaultPrefs.putBoolean(key, state)
    private fun getBooleanState(key: String, default: Boolean = false) = defaultPrefs.getBoolean(key, default)
}