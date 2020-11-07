package com.crazylegend.vigilante.power.db

import javax.inject.Inject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class PowerRepository @Inject constructor(private val powerDAO: PowerDAO) {

    fun getAllPowerActions() = powerDAO.getAllPowerActions()

    suspend fun getPowerModelById(id: Int) = powerDAO.getPowerActionByID(id)
    suspend fun insertPowerAction(powerModel: PowerModel) = powerDAO.insertPowerAction(powerModel)
}