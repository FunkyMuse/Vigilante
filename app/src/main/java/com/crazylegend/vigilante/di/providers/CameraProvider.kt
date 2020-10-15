package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.lifecycle.*
import com.crazylegend.coroutines.cancelIfActive
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
class CameraProvider @Inject constructor(@ServiceContext private val context: Context) : LifecycleOwner, LifecycleObserver, ServiceCoroutines {

    //coroutines
    override lateinit var job: Job

    //lifecycle
    private val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)
    override fun getLifecycle(): Lifecycle = serviceLifecycleDispatcher.lifecycle

    //camera
    private lateinit var cameraCallback: CameraManager.AvailabilityCallback
    var packageUsingCamera: String? = null
    var wasCameraBeingUsed = false
    var cameraStartedUsageTime: Long? = null

    fun initLifecycle() {
        serviceLifecycleDispatcher.onServicePreSuperOnCreate()
        serviceLifecycleDispatcher.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initVars() {
        debug { "CREATED" }
        job = Job()
        cameraCallback = cameraListener()
        context.cameraManager.registerAvailabilityCallback(cameraCallback, null)
    }

    fun setServiceConnected() {
        serviceLifecycleDispatcher.onServicePreSuperOnStart()
    }

    private fun cameraListener(): CameraManager.AvailabilityCallback {
        return object : CameraManager.AvailabilityCallback() {
            override fun onCameraAvailable(cameraId: String) {
                super.onCameraAvailable(cameraId)
                setCameraNotUsed()
            }

            override fun onCameraUnavailable(cameraId: String) {
                super.onCameraUnavailable(cameraId)
                setCameraUsed()
            }
        }
    }

    private fun setCameraUsed() {
        wasCameraBeingUsed = true
        debug { "CAMERA IS BEING USED by $packageUsingCamera" }
    }

    private fun setCameraNotUsed() {
        if (wasCameraBeingUsed) {
            packageUsingCamera = null
            wasCameraBeingUsed = false
            cameraStartedUsageTime = null
            val cameraStoppedBeingUsedAt = currentTimeMillis
            debug { "CAMERA NOT USED ANYMORE" }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeResources() {
        debug { "DESTROYED" }
        job.cancelIfActive()
        context.cameraManager.unregisterAvailabilityCallback(cameraCallback)
    }

    fun cleanUp() {
        debug { "DESTROYING" }
        serviceLifecycleDispatcher.onServicePreSuperOnDestroy()
    }

    fun onStart() {
        serviceLifecycleDispatcher.onServicePreSuperOnStart()
    }

    fun eventAction(eventPackageName: CharSequence) {
        if (!wasCameraBeingUsed) {
            packageUsingCamera = eventPackageName.toString()
        } else {
            if (cameraStartedUsageTime == null)
                cameraStartedUsageTime = currentTimeMillis
        }
    }


}