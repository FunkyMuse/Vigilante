package com.crazylegend.vigilante.di.providers.prefs.camera

import android.content.SharedPreferences
import com.crazylegend.sharedpreferences.putBoolean
import com.crazylegend.vigilante.di.providers.prefs.contracts.DotContract
import com.crazylegend.vigilante.di.providers.prefs.contracts.NotificationsContract
import com.crazylegend.vigilante.di.providers.prefs.customization.CustomizationPrefs
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.CAMERA_BYPASS_DND_PREF_KEY
import com.crazylegend.vigilante.settings.CAMERA_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.settings.CAMERA_DOT_PREF_KEY
import com.crazylegend.vigilante.settings.CAMERA_NOTIFICATIONS_PREF_KEY
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
@Singleton
class CameraPrefs @Inject constructor(
        private val customizationPrefs: CustomizationPrefs,
        @EncryptedPrefs private val defaultPrefs: SharedPreferences,
) : CameraPreferences, DotContract, NotificationsContract {

    //region dot status
    override val isDotEnabled get() = defaultPrefs.getBoolean(CAMERA_DOT_PREF_KEY, true)
    override fun setDotStatus(status: Boolean) = defaultPrefs.putBoolean(CAMERA_DOT_PREF_KEY, status)
    //endregion

    //region notifications status
    override val areNotificationsEnabled
        get() = defaultPrefs.getBoolean(
                CAMERA_NOTIFICATIONS_PREF_KEY,
                true
        )

    override fun updateNotificationsValue(value: Boolean) =
            defaultPrefs.putBoolean(CAMERA_NOTIFICATIONS_PREF_KEY, value)
    //endregion

    //region bypass dnd
    override val isBypassDNDEnabled get() = defaultPrefs.getBoolean(CAMERA_BYPASS_DND_PREF_KEY, false)
    override fun updateDNDValue(value: Boolean) =
            defaultPrefs.putBoolean(CAMERA_BYPASS_DND_PREF_KEY, value)
    //endregion

    override val getCameraColorPref
        get() = customizationPrefs.getColorPref(
                CAMERA_CUSTOMIZATION_BASE_PREF
        )
    override val getCameraSizePref
        get() = customizationPrefs.getSizePref(
                CAMERA_CUSTOMIZATION_BASE_PREF
        )
    override val getCameraPositionPref
        get() = customizationPrefs.getPositionPref(
                CAMERA_CUSTOMIZATION_BASE_PREF
        )
    override val getLayoutCameraPositionPref
        get() = customizationPrefs.getLayoutPosition(
                getCameraPositionPref
        )
    override val getCameraNotificationLEDColorPref
        get() = customizationPrefs.getNotificationColorPref(
                CAMERA_CUSTOMIZATION_BASE_PREF
        )
    override val getCameraSpacing
        get() = customizationPrefs.getPositionSpacing(
                CAMERA_CUSTOMIZATION_BASE_PREF
        )
    override val getCameraVibrationPositionPref
        get() = customizationPrefs.getVibrationPref(
                CAMERA_CUSTOMIZATION_BASE_PREF
        )
    override val getCameraVibrationEffectPref
        get() = customizationPrefs.getVibrationEffect(
                getCameraVibrationPositionPref
        )


}