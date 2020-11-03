package com.crazylegend.vigilante.camera

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.database.coroutines.makeDBCall
import com.crazylegend.kotlinextensions.context.cameraManager
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.vigilante.VigilanteService.Companion.currentPackageString
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.camera.db.CameraRepository
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * Created by crazy on 10/15/20 to long live and prosper !
 */
@ServiceScoped
class CameraProcessor @Inject constructor(
        @ServiceContext private val context: Context,
        private val cameraRepository: CameraRepository) : ServiceManagersCoroutines {

    //lifecycle
    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    //camera
    private lateinit var cameraCallback: CameraManager.AvailabilityCallback
    private val packageUsingCamera: AtomicReference<String?> = AtomicReference(currentPackageString)
    private var wasCameraBeingUsed: AtomicBoolean = AtomicBoolean(false)
    private val cameraStartedUsageTime: AtomicLong = AtomicLong(-1)

    override fun initVars() {
        cameraCallback = cameraListener()
    }

    override fun registerCallbacks() {
        context.cameraManager.registerAvailabilityCallback(cameraCallback, null)
    }

    override fun disposeResources() {
        context.cameraManager.unregisterAvailabilityCallback(cameraCallback)
    }

    //region private
    private fun cameraListener(): CameraManager.AvailabilityCallback =
            object : CameraManager.AvailabilityCallback() {
                override fun onCameraAvailable(cameraId: String) {
                    super.onCameraAvailable(cameraId)
                    setCameraNotUsed(cameraId)
                }

                override fun onCameraUnavailable(cameraId: String) {
                    super.onCameraUnavailable(cameraId)
                    setCameraUsed()
                }
            }

    private fun setCameraUsed() {
        wasCameraBeingUsed.set(true)
        packageUsingCamera.set(currentPackageString)
    }

    private fun setCameraNotUsed(cameraId: String) {
        if (wasCameraBeingUsed.get()) {
            val cameraStoppedBeingUsedAt = currentTimeMillis
            val cameraModel = CameraModel(Date(cameraStartedUsageTime.getAndSet(-1)),
                    packageUsingCamera.getAndSet(null), cameraId, Date(cameraStoppedBeingUsedAt))
            scope.makeDBCall {
                cameraRepository.insertCameraRecord(cameraModel)
            }
            wasCameraBeingUsed.set(false)
        }
    }
    //endregion


    //region public
    override fun eventActionByPackageName(eventPackageName: CharSequence) {
        if (!wasCameraBeingUsed.get()) {
            packageUsingCamera.set(eventPackageName.toString())
        } else {
            if (cameraStartedUsageTime.get() == -1L)
                cameraStartedUsageTime.set(currentTimeMillis)
        }
    }
    //endregion


}