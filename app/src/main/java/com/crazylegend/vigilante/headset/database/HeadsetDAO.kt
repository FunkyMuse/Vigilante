package com.crazylegend.vigilante.headset.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */

@Dao
interface HeadsetDAO {

    @Query("select * from headsetAccesses order by headsetActionTime desc")
    fun getAllHeadsetRecords(): PagingSource<Int, HeadsetModel>

    @Insert
    suspend fun addHeadsetRecord(cameraModel: HeadsetModel)

    @Delete
    suspend fun removeHeadsetRecord(cameraModel: HeadsetModel)
}