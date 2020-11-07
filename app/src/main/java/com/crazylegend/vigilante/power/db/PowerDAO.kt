package com.crazylegend.vigilante.power.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */

@Dao
interface PowerDAO {

    @Query("select * from powerActions")
    fun getAllPowerActions(): PagingSource<Int, PowerModel>

    @Query("select count(*) from powerActions where isCharging = 0")
    fun getDischargingCountTotal(): Flow<Int>

    @Query("select count(*) from powerActions where isCharging = 1")
    fun getChargingCountTotal(): Flow<Int>

    @Query("select * from powerActions where id=:queryID")
    suspend fun getPowerActionByID(queryID: Int): PowerModel?

    @Insert
    suspend fun insertPowerAction(powerModel: PowerModel)
}