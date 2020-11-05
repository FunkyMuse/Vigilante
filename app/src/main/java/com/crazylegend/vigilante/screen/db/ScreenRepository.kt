package com.crazylegend.vigilante.screen.db

import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class ScreenRepository @Inject constructor(private val dao: ScreenDAO) {

    suspend fun insertScreenModel(screenModel: ScreenModel) = dao.insertScreenAction(screenModel)

    fun getAllScreenLocks() = dao.getAllScreenLocks()

    fun getAllScreenUnlocks() = dao.getAllScreenUnlocks()

    fun getAllScreenActions() = dao.getAllScreenActions()


    fun totalLocks() = dao.getTotalLocks()
    fun totalUnlocks() = dao.getTotalUnlocks()
    fun totalActions() = dao.getTotalActionsCount()
}