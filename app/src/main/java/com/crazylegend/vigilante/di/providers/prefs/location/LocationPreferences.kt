package com.crazylegend.vigilante.di.providers.prefs.location

/**
 * Created by funkymuse on 6/13/21 to long live and prosper !
 */
interface LocationPreferences {
    val getLocationColorPref: Int
    val getLocationSizePref: Float
    val getLocationPositionPref: Int
    val getLayoutLocationPositionPref: Int
    val getLocationNotificationLEDColorPref: Int
    val getLocationSpacing: Int
    val getLocationVibrationPositionPref: Int
    val getLocationVibrationEffectPref: LongArray?
}