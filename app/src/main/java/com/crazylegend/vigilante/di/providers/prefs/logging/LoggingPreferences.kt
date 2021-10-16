package com.crazylegend.vigilante.di.providers.prefs.logging

/**
 * Created by funkymuse on 10/16/21 to long live and prosper !
 */
interface LoggingPreferences {

    val isPermissionLoggingEnabled: Boolean
    fun setPermissionLoggingState(isEnabled: Boolean)

    val isPowerLoggingEnabled: Boolean
    fun setPowerLoggingState(isEnabled: Boolean)

    val isHeadsetLoggingEnabled: Boolean
    fun setHeadsetLoggingState(isEnabled: Boolean)

    val isNotificationsLoggingEnabled: Boolean
    fun setNotificationsLoggingState(isEnabled: Boolean)

    val isLockScreenLoggingEnabled: Boolean
    fun setLockScreenLoggingState(isEnabled: Boolean)

}