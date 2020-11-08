package com.crazylegend.vigilante.di.providers

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean
import com.crazylegend.kotlinextensions.sharedprefs.putString
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.*
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */

class PrefsProvider @Inject constructor(@EncryptedPrefs
                                        private val defaultPrefs: SharedPreferences) {

    val getDateFormat get() = defaultPrefs.getString(DATE_PREF_KEY, DEFAULT_DATE) ?: DEFAULT_DATE

    val areNotificationsEnabled get() = defaultPrefs.getBoolean(NOTIFICATIONS_PREF_KEY, false)
    fun updateNotificationsValue(value: Boolean) = defaultPrefs.putBoolean(NOTIFICATIONS_PREF_KEY, value)

    val isDarkThemeEnabled get() = defaultPrefs.getBoolean(THEME_PREF_KEY, false)

    fun changeTheme() {
        defaultPrefs.edit(true) { putBoolean(THEME_PREF_KEY, !isDarkThemeEnabled) }
        applyThemeLogic()
    }

    fun updateDateFormat(value: String) = defaultPrefs.putString(DATE_PREF_KEY, value)

    val isDotEnabled get() = defaultPrefs.getBoolean(DOT_PREF_KEY, false)
    fun setDotStatus(status: Boolean) = defaultPrefs.putBoolean(DOT_PREF_KEY, status)

    fun applyThemeLogic() {
        val themeMode = if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    val isVigilanteExcludedFromNotifications get() = defaultPrefs.getBoolean(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY, false)
    fun setExcludeVigilanteFromNotificationsStatus(newValue: Boolean) = defaultPrefs.putBoolean(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY, newValue)
}