package com.crazylegend.vigilante.di.providers.prefs.location

import android.content.SharedPreferences
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean
import com.crazylegend.vigilante.di.providers.prefs.contracts.DotContract
import com.crazylegend.vigilante.di.providers.prefs.contracts.NotificationsContract
import com.crazylegend.vigilante.di.providers.prefs.customization.CustomizationPrefs
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.LOCATION_BYPASS_DND_PREF_KEY
import com.crazylegend.vigilante.settings.LOCATION_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.settings.LOCATION_DOT_PREF_KEY
import com.crazylegend.vigilante.settings.LOCATION_NOTIFICATIONS_PREF_KEY
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by funkymuse on 6/13/21 to long live and prosper !
 */
@Singleton
class LocationPrefs @Inject constructor(
        private val customizationPrefs: CustomizationPrefs,
        @EncryptedPrefs private val defaultPrefs: SharedPreferences,
) : LocationPreferences, DotContract, NotificationsContract {

    //region dot status
    override val isDotEnabled get() = defaultPrefs.getBoolean(LOCATION_DOT_PREF_KEY, true)
    override fun setDotStatus(status: Boolean) = defaultPrefs.putBoolean(LOCATION_DOT_PREF_KEY, status)
    //endregion

    //region notifications status
    override val areNotificationsEnabled
        get() = defaultPrefs.getBoolean(
                LOCATION_NOTIFICATIONS_PREF_KEY,
                true
        )

    override fun updateNotificationsValue(value: Boolean) =
            defaultPrefs.putBoolean(LOCATION_NOTIFICATIONS_PREF_KEY, value)
    //endregion

    //region bypass dnd
    override val isBypassDNDEnabled get() = defaultPrefs.getBoolean(LOCATION_BYPASS_DND_PREF_KEY, false)
    override fun updateDNDValue(value: Boolean) =
            defaultPrefs.putBoolean(LOCATION_BYPASS_DND_PREF_KEY, value)
    //endregion

    override val getLocationColorPref
        get() = customizationPrefs.getColorPref(
                LOCATION_CUSTOMIZATION_BASE_PREF
        )
    override val getLocationSizePref
        get() = customizationPrefs.getSizePref(
                LOCATION_CUSTOMIZATION_BASE_PREF
        )
    override val getLocationPositionPref
        get() = customizationPrefs.getPositionPref(
                LOCATION_CUSTOMIZATION_BASE_PREF
        )
    override val getLayoutLocationPositionPref
        get() = customizationPrefs.getLayoutPosition(
                getLocationPositionPref
        )
    override val getLocationNotificationLEDColorPref
        get() = customizationPrefs.getNotificationColorPref(
                LOCATION_CUSTOMIZATION_BASE_PREF
        )
    override val getLocationSpacing
        get() = customizationPrefs.getPositionSpacing(
                LOCATION_CUSTOMIZATION_BASE_PREF
        )
    override val getLocationVibrationPositionPref
        get() = customizationPrefs.getVibrationPref(
                LOCATION_CUSTOMIZATION_BASE_PREF
        )
    override val getLocationVibrationEffectPref
        get() = customizationPrefs.getVibrationEffect(
                getLocationVibrationPositionPref
        )
}