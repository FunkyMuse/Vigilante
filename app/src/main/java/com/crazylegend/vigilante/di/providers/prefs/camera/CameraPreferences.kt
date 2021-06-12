package com.crazylegend.vigilante.di.providers.prefs.camera

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
interface CameraPreferences {
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
}