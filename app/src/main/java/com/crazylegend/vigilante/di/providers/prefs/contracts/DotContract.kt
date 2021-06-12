package com.crazylegend.vigilante.di.providers.prefs.contracts

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
interface DotContract {
    //region dot status
    val isDotEnabled: Boolean
    fun setDotStatus(status: Boolean)
    //endregion
}