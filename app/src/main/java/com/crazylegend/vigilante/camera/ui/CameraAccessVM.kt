package com.crazylegend.vigilante.camera.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.camera.db.CameraRepository

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class CameraAccessVM @ViewModelInject constructor(
        private val cameraRepository: CameraRepository,
        application: Application) : AbstractAVM(application) {

    val cameraAccess = Pager(pagingConfig) {
        cameraRepository.getAllCameraRecords()
    }.flow.cachedIn(viewModelScope)

}