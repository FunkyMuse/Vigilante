package com.crazylegend.vigilante.camera.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */

@Dao
interface CameraDao {

    @Query("select * from cameraAccesses")
    fun getAllCameraRecords(): Flow<List<CameraModel>>

    @Insert
    suspend fun addCameraRecord(cameraModel: CameraModel)

    @Delete
    suspend fun removeCameraRecord(cameraModel: CameraModel)
}