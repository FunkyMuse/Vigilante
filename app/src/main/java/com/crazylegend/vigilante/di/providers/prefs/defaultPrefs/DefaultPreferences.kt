package com.crazylegend.vigilante.di.providers.prefs.defaultPrefs

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
interface DefaultPreferences {

    //region date format
    fun updateDateFormat(value: String)
    val getDateFormat: String
    //endregion

    //region theme
    val isDarkThemeEnabled: Boolean
    fun changeTheme()
    //endregion

    //region notification exclusion
    val isVigilanteExcludedFromNotifications: Boolean
    fun setExcludeVigilanteFromNotificationsStatus(newValue: Boolean)
    //endregion

    //region intro
    val isIntroShown: Boolean
    fun setIntroShown()
    //endregion

    //region biometric auth
    val isBiometricAuthEnabled: Boolean
    fun updateBiometricStatus(status: Boolean)
    //endregion

    //region history deletion
    fun scheduleDeletionHistory()
    fun cancelDeletionHistory()
    //endregion

}