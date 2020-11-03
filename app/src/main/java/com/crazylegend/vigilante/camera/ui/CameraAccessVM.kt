package com.crazylegend.vigilante.camera.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.database.DBResult
import com.crazylegend.database.coroutines.makeDBCallListFlow
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.camera.db.CameraRepository

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class CameraAccessVM @ViewModelInject constructor(
        private val cameraRepository: CameraRepository,
        application: Application) : AndroidViewModel(application) {

    private val cameraDataList: MutableLiveData<DBResult<List<CameraModel>>> = MutableLiveData()
    val cameraAccess: LiveData<DBResult<List<CameraModel>>> = cameraDataList

    init {
        getAll()
    }

    private fun getAll() {
        makeDBCallListFlow(cameraDataList) {
            cameraRepository.getAllCameraRecords()
        }
    }
}