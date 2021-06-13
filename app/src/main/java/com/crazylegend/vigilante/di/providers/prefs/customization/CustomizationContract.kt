package com.crazylegend.vigilante.di.providers.prefs.customization

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
interface CustomizationContract {

    //region notification
    fun saveNotificationColorPref(prefBaseName: String, pickedColor: Int)
    fun getNotificationColorPref(basePref: String): Int
    //endregion

    //region size
    fun saveSizePref(prefBaseName: String, sizeSlider: Float)
    fun getSizePref(basePref: String): Float
    //endregion

    //region vibration
    fun getVibrationPref(basePref: String): Int
    fun getVibrationEffect(pref: Int): LongArray?
    //endregion

    //region color
    fun saveColorPref(prefBaseName: String, pickedColor: Int)
    fun getColorPref(basePref: String): Int
    //endregion

    //region spacing
    fun saveSpacing(basePref: String, spacing: Int)
    fun getPositionSpacing(basePref: String): Int
    //endregion

    //region layout position
    fun getPositionPref(basePref: String): Int
    fun savePositionPref(prefBaseName: String, position: Int)
    fun getLayoutPosition(pref: Int): Int
    //endregion
}