package com.crazylegend.vigilante.di.providers.prefs.contracts

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
interface NotificationsContract {

    //region bypass dnd
    val isBypassDNDEnabled: Boolean
    fun updateDNDValue(value: Boolean)
    //endregion

    //region sound
    val isSoundEnabled: Boolean
    fun updateSoundValue(value: Boolean)
    //endregion

    //region notifications status
    val areNotificationsEnabled: Boolean
    fun updateNotificationsValue(value: Boolean)
    //endregion
}