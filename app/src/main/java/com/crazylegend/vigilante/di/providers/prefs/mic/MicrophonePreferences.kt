package com.crazylegend.vigilante.di.providers.prefs.mic

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
interface MicrophonePreferences {
    val getMicColorPref: Int
    val getMicSizePref: Float
    val getMicPositionPref: Int
    val getLayoutMicPositionPref: Int
    val getMicNotificationLEDColorPref: Int
    val getMicSpacing: Int
    val getMicVibrationPositionPref: Int
    val getMicVibrationEffectPref: LongArray?
}