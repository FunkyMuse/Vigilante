package com.crazylegend.vigilante.headset.database

import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class HeadsetRepository @Inject constructor(private val headsetDAO: HeadsetDAO) {

    fun getAllHeadsetRecords() = headsetDAO.getAllHeadsetRecords()
    suspend fun insertHeadsetRecord(cameraModel: HeadsetModel) = headsetDAO.addHeadsetRecord(cameraModel)
    suspend fun deleteHeadsetRecord(cameraModel: HeadsetModel) = headsetDAO.removeHeadsetRecord(cameraModel)
}