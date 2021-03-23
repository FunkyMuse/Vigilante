package com.crazylegend.vigilante.di.providers.prefs

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
interface DefaultPreferences {

    //region date format
    fun updateDateFormat(value: String)
    val getDateFormat: String
    //endregion

    //region notifications status
    val areNotificationsEnabled: Boolean
    fun updateNotificationsValue(value: Boolean)
    //endregion

    //region theme
    val isDarkThemeEnabled: Boolean
    fun changeTheme()
    //endregion

    //region dot status
    val isDotEnabled: Boolean
    fun setDotStatus(status: Boolean)
    //endregion

    //region notification exclusion
    val isVigilanteExcludedFromNotifications: Boolean
    fun setExcludeVigilanteFromNotificationsStatus(newValue: Boolean)
    //endregion

    //region camera prefs
    val getCameraColorPref: Int
    val getCameraSizePref: Float
    val getCameraPositionPref: Int
    val getLayoutCameraPositionPref: Int
    val getCameraNotificationLEDColorPref: Int
    val getCameraSpacing: Int
    val getCameraVibrationPositionPref: Int
    val getCameraVibrationEffectPref: LongArray?
    //endregion

    //region mic prefs
    val getMicColorPref: Int
    val getMicSizePref: Float
    val getMicPositionPref: Int
    val getLayoutMicPositionPref: Int
    val getMicNotificationLEDColorPref: Int
    val getMicSpacing: Int
    val getMicVibrationPositionPref: Int
    val getMicVibrationEffectPref: LongArray?
    //endregion


    //region intro
    val isIntroShown: Boolean
    fun setIntroShown()
    //endregion

    //region biometric auth
    val isBiometricAuthEnabled: Boolean
    fun updateBiometricStatus(status: Boolean)
    //endregion

    //region bypass dnd
    val isBypassDNDEnabled: Boolean
    fun updateDNDValue(value: Boolean)
    //endregion

}