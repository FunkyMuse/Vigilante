package com.crazylegend.vigilante.di.providers

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.NOTIFICATIONS_PREF_KEY
import com.crazylegend.vigilante.settings.THEME_PREF_KEY
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */

class PrefsProvider @Inject constructor(@EncryptedPrefs
                                        private val defaultPrefs: SharedPreferences) {

    val notificationsStatus get() = defaultPrefs.getBoolean(NOTIFICATIONS_PREF_KEY, false)
    fun updateNotificationsValue(value: Boolean) = defaultPrefs.putBoolean(NOTIFICATIONS_PREF_KEY, value)

    val isDarkThemeEnabled get() = defaultPrefs.getBoolean(THEME_PREF_KEY, false)

    fun changeTheme() {
        defaultPrefs.edit(true) { putBoolean(THEME_PREF_KEY, !isDarkThemeEnabled) }
        applyThemeLogic()
    }

    fun applyThemeLogic() {
        val themeMode = if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }
}