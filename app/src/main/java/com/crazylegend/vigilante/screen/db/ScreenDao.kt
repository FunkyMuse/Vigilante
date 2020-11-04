package com.crazylegend.vigilante.screen.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */

@Dao
interface ScreenDao {

    @Query("select * from screenActions")
    fun getAllScreenActions(): PagingSource<Int, ScreenModel>

    @Query("select * from screenActions where wasScreenLocked = 0")
    fun getAllScreenUnlocks(): PagingSource<Int, ScreenModel>

    @Query("select * from screenActions where wasScreenLocked = 1")
    fun getAllScreenLocks(): PagingSource<Int, ScreenModel>

    @Query("select count(*) from screenActions")
    fun getTotalActionsCount(): Flow<Int>

    @Query("select count(wasScreenLocked) from screenActions where wasScreenLocked = 1")
    fun getTotalLocks(): Flow<Int>

    @Query("select count(wasScreenLocked) from screenActions where wasScreenLocked = 0")
    fun getTotalUnlocks(): Flow<Int>

    @Insert
    suspend fun insertScreenAction(screenModel: ScreenModel)

}