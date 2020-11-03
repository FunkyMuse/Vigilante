package com.crazylegend.vigilante.microphone.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@Dao
interface MicrophoneDAO {

    @Query("select * from microphoneAccesses")
    fun getAllMicrophoneRecords(): Flow<List<MicrophoneModel>>

    @Insert
    suspend fun addMicrophoneRecord(microphone: MicrophoneModel)

    @Delete
    suspend fun removeMicrophoneRecord(microphone: MicrophoneModel)
}