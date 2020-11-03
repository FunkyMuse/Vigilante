package com.crazylegend.vigilante.microphone.db

import javax.inject.Inject

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class MicrophoneRepository @Inject constructor(private val cameraDao: MicrophoneDAO) {

    fun getAllMicrophoneRecords() = cameraDao.getAllMicrophoneRecords()

    suspend fun insertMicrophoneRecord(cameraModel: MicrophoneModel) = cameraDao.addMicrophoneRecord(cameraModel)

    suspend fun deleteMicrophoneRecord(cameraModel: MicrophoneModel) = cameraDao.removeMicrophoneRecord(cameraModel)
}