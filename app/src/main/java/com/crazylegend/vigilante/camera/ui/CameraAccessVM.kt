package com.crazylegend.vigilante.camera.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.camera.db.CameraRepository

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class CameraAccessVM @ViewModelInject constructor(
        private val cameraRepository: CameraRepository,
        application: Application) : AbstractAVM(application) {

    val cameraAccess = provideDatabaseData { cameraRepository.getAllCameraRecords() }

}