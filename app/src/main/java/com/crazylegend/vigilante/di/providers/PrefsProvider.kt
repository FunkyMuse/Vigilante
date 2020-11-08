package com.crazylegend.vigilante.di.providers

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean
import com.crazylegend.kotlinextensions.sharedprefs.putFloat
import com.crazylegend.kotlinextensions.sharedprefs.putInt
import com.crazylegend.kotlinextensions.sharedprefs.putString
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.COLOR_PREF_ADDITION
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.POSITION_PREF_ADDITION
import com.crazylegend.vigilante.customization.CustomizationFragment.Companion.SIZE_PREF_ADDITION
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.*
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */

class PrefsProvider @Inject constructor(@EncryptedPrefs
                                        private val defaultPrefs: SharedPreferences) {

    companion object {
        const val DEFAULT_LAYOUT_POSITION = 0
        const val DEFAULT_DOT_COLOR = 31727
        const val DEFAULT_DOT_SIZE = 8f
    }

    val getDateFormat get() = defaultPrefs.getString(DATE_PREF_KEY, DEFAULT_DATE) ?: DEFAULT_DATE

    val areNotificationsEnabled get() = defaultPrefs.getBoolean(NOTIFICATIONS_PREF_KEY, false)
    fun updateNotificationsValue(value: Boolean) = defaultPrefs.putBoolean(NOTIFICATIONS_PREF_KEY, value)

    //region theme
    val isDarkThemeEnabled get() = defaultPrefs.getBoolean(THEME_PREF_KEY, false)
    fun changeTheme() {
        defaultPrefs.edit(true) { putBoolean(THEME_PREF_KEY, !isDarkThemeEnabled) }
        applyThemeLogic()
    }

    fun applyThemeLogic() {
        val themeMode = if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }
    //endregion

    fun updateDateFormat(value: String) = defaultPrefs.putString(DATE_PREF_KEY, value)

    val isDotEnabled get() = defaultPrefs.getBoolean(DOT_PREF_KEY, false)
    fun setDotStatus(status: Boolean) = defaultPrefs.putBoolean(DOT_PREF_KEY, status)

    val isVigilanteExcludedFromNotifications get() = defaultPrefs.getBoolean(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY, false)
    fun setExcludeVigilanteFromNotificationsStatus(newValue: Boolean) = defaultPrefs.putBoolean(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY, newValue)

    fun saveColorPref(prefBaseName: String, pickedColor: Int) = defaultPrefs.putInt(prefBaseName, pickedColor)
    fun saveSizePref(prefBaseName: String, sizeSlider: Float) = defaultPrefs.putFloat(prefBaseName, sizeSlider)
    fun savePositionPref(prefBaseName: String, position: Int) = defaultPrefs.putInt(prefBaseName, position)

    val getCameraColorPref get() = getColorPref(CAMERA_CUSTOMIZATION_BASE_PREF)
    val getCameraSizePref get() = getSizePref(CAMERA_CUSTOMIZATION_BASE_PREF)
    val getCameraPositionPref get() = getPositionPref(CAMERA_CUSTOMIZATION_BASE_PREF)

    val getMicColorPref get() = getColorPref(MIC_CUSTOMIZATION_BASE_PREF)
    val getMicSizePref get() = getSizePref(MIC_CUSTOMIZATION_BASE_PREF)
    val getMicPositionPref get() = getPositionPref(MIC_CUSTOMIZATION_BASE_PREF)


    private fun getColorPref(basePref: String) = defaultPrefs.getInt(basePref + COLOR_PREF_ADDITION, DEFAULT_DOT_COLOR)
    private fun getSizePref(basePref: String) = defaultPrefs.getFloat(basePref + SIZE_PREF_ADDITION, DEFAULT_DOT_SIZE)

    /**
    0 is top right corner
    1 is top left corner

    2 is center right side
    3 is center left side

    4 is bottom right corner
    5 is bottom left corner
     * @return Int
     */
    private fun getPositionPref(basePref: String) = defaultPrefs.getInt(basePref + POSITION_PREF_ADDITION, DEFAULT_LAYOUT_POSITION)


}