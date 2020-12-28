package com.crazylegend.vigilante.camera

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.kotlinextensions.context.cameraManager
import com.crazylegend.kotlinextensions.context.notificationManager
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.di.providers.UserNotificationsProvider
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.service.VigilanteService
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/15/20 to long live and prosper !
 */
@ServiceScoped
class CameraProcessor @Inject constructor(
        @ServiceContext private val context: Context,
        private val userNotificationsProvider: UserNotificationsProvider,
        private val prefsProvider: PrefsProvider) : ServiceManagersCoroutines {

    //lifecycle
    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    //camera
    private lateinit var cameraCallback: CameraManager.AvailabilityCallback
    private val cameraNotificationID = 69

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
                    setCameraNotUsed()
                    stopNotificationIfUserEnabled()
                }

                override fun onCameraUnavailable(cameraId: String) {
                    super.onCameraUnavailable(cameraId)
                    setCameraUsed()
                }
            }


    private fun stopNotificationIfUserEnabled() {
        context.notificationManager?.cancel(cameraNotificationID)
    }

    private fun setCameraUsed() {
        sendNotificationIfUserEnabled()
        VigilanteService.serviceLayoutListener?.showCamera()
    }

    private fun sendNotificationIfUserEnabled() {
        prefsProvider.areNotificationsEnabled.ifTrue {
            userNotificationsProvider.buildUsageNotification(cameraNotificationID, R.string.cam_being_used, prefsProvider.getCameraNotificationLEDColorPref)
        }
    }


    private fun setCameraNotUsed() {
        VigilanteService.serviceLayoutListener?.hideCamera()
    }
    //endregion


}