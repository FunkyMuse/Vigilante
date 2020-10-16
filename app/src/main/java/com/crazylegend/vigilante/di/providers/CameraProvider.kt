package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.kotlinextensions.context.cameraManager
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.utils.ServiceCoroutines
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.Job
import javax.inject.Inject

/**
 * Created by crazy on 10/15/20 to long live and prosper !
 */
@ServiceScoped
class CameraProvider @Inject constructor(@ServiceContext private val context: Context) : ServiceCoroutines {

    //coroutines
    override lateinit var job: Job

    //lifecycle
    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    //camera
    private lateinit var cameraCallback: CameraManager.AvailabilityCallback
    private var packageUsingCamera: String? = null
    private var wasCameraBeingUsed = false
    private var cameraStartedUsageTime: Long? = null

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
                    setCameraUsed(cameraId)
                }
            }

    private fun setCameraUsed(cameraId: String) {
        wasCameraBeingUsed = true
        debug { "CAMERA $cameraId IS BEING USED by $packageUsingCamera" }
    }

    private fun setCameraNotUsed(cameraId: String) {
        if (wasCameraBeingUsed) {
            packageUsingCamera = null
            wasCameraBeingUsed = false
            cameraStartedUsageTime = null
            val cameraStoppedBeingUsedAt = currentTimeMillis
            debug { "CAMERA $cameraId NOT USED ANYMORE" }
        }
    }
    //endregion


    //region public
    override fun eventAction(eventPackageName: CharSequence) {
        if (!wasCameraBeingUsed) {
            packageUsingCamera = eventPackageName.toString()
        } else {
            if (cameraStartedUsageTime == null)
                cameraStartedUsageTime = currentTimeMillis
        }
    }
    //endregion


}