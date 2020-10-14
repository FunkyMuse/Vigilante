package com.crazylegend.vigilante

import android.accessibilityservice.AccessibilityService
import android.hardware.camera2.CameraManager
import android.view.accessibility.AccessibilityEvent
import com.crazylegend.kotlinextensions.context.cameraManager
import com.crazylegend.kotlinextensions.dateAndTime.getCurrentTimeInFormat
import com.crazylegend.kotlinextensions.log.debug
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@AndroidEntryPoint
class VigilanteService : AccessibilityService() {

    private val dateFormat = "dd/MM/yyyy hh:mm:ss"

    //camera
    private lateinit var cameraCallback: CameraManager.AvailabilityCallback
    private var packageUsingCamera: String? = null
    private var wasCameraBeingUsed = false
    private var cameraStartedUsageTime: String? = null
    private val currentDate get() = getCurrentTimeInFormat(dateFormat)

    override fun onCreate() {
        super.onCreate()
        cameraCallback = cameraListener()
    }

    override fun onServiceConnected() {
        cameraManager.registerAvailabilityCallback(cameraCallback, null)
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
            debug {
                "CAMERA IS NOT USED ANYMORE at $currentDate \n" +
                        "STARTED USAGE AT $cameraStartedUsageTime"
            }
            cameraStartedUsageTime = null
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        rememberEventPackageName(event)
    }

    private fun rememberEventPackageName(event: AccessibilityEvent) {
        val eventPackageName = event.packageName
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && eventPackageName != null) {
            if (!wasCameraBeingUsed) {
                packageUsingCamera = eventPackageName.toString()
            } else {
                if (cameraStartedUsageTime == null)
                    cameraStartedUsageTime = currentDate

                debug { "CAMERA STARTED BEING USED AT $currentDate" }
            }
        }
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.unregisterAvailabilityCallback(cameraCallback)
    }
}