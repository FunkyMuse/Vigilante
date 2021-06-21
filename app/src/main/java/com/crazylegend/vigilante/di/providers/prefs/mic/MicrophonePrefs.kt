package com.crazylegend.vigilante.di.providers.prefs.mic

import android.content.SharedPreferences
import com.crazylegend.sharedpreferences.putBoolean
import com.crazylegend.vigilante.di.providers.prefs.contracts.DotContract
import com.crazylegend.vigilante.di.providers.prefs.contracts.NotificationsContract
import com.crazylegend.vigilante.di.providers.prefs.customization.CustomizationPrefs
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.settings.MIC_BYPASS_DND_PREF_KEY
import com.crazylegend.vigilante.settings.MIC_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.settings.MIC_DOT_PREF_KEY
import com.crazylegend.vigilante.settings.MIC_NOTIFICATIONS_PREF_KEY
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
@Singleton
class MicrophonePrefs @Inject constructor(
    private val customizationPrefs: CustomizationPrefs,
    @EncryptedPrefs private val defaultPrefs: SharedPreferences
) : MicrophonePreferences, DotContract, NotificationsContract {

    //region dot status
    override val isDotEnabled get() = defaultPrefs.getBoolean(MIC_DOT_PREF_KEY, true)
    override fun setDotStatus(status: Boolean) = defaultPrefs.putBoolean(MIC_DOT_PREF_KEY, status)
    //endregion

    //region notifications status
    override val areNotificationsEnabled
        get() = defaultPrefs.getBoolean(MIC_NOTIFICATIONS_PREF_KEY, true)

    override fun updateNotificationsValue(value: Boolean) =
        defaultPrefs.putBoolean(MIC_NOTIFICATIONS_PREF_KEY, value)
    //endregion

    //region bypass dnd
    override val isBypassDNDEnabled get() = defaultPrefs.getBoolean(MIC_BYPASS_DND_PREF_KEY, false)
    override fun updateDNDValue(value: Boolean) =
        defaultPrefs.putBoolean(MIC_BYPASS_DND_PREF_KEY, value)
    //endregion


    override val getMicColorPref get() = customizationPrefs.getColorPref(MIC_CUSTOMIZATION_BASE_PREF)
    override val getMicSizePref get() = customizationPrefs.getSizePref(MIC_CUSTOMIZATION_BASE_PREF)
    override val getMicPositionPref
        get() = customizationPrefs.getPositionPref(
            MIC_CUSTOMIZATION_BASE_PREF
        )
    override val getLayoutMicPositionPref
        get() = customizationPrefs.getLayoutPosition(
            getMicPositionPref
        )
    override val getMicNotificationLEDColorPref
        get() = customizationPrefs.getNotificationColorPref(
            MIC_CUSTOMIZATION_BASE_PREF
        )
    override val getMicSpacing
        get() = customizationPrefs.getPositionSpacing(
            MIC_CUSTOMIZATION_BASE_PREF
        )
    override val getMicVibrationPositionPref
        get() = customizationPrefs.getVibrationPref(
            MIC_CUSTOMIZATION_BASE_PREF
        )
    override val getMicVibrationEffectPref
        get() = customizationPrefs.getVibrationEffect(
            getMicVibrationPositionPref
        )
}