package com.crazylegend.vigilante.camera.db

import javax.inject.Inject

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class CameraRepository @Inject constructor(private val cameraDao: CameraDao) {

    fun getAllCameraRecords() = cameraDao.getAllCameraRecords()

    suspend fun insertCameraRecord(cameraModel: CameraModel) = cameraDao.addCameraRecord(cameraModel)

    suspend fun deleteCameraRecord(cameraModel: CameraModel) = cameraDao.removeCameraRecord(cameraModel)
}