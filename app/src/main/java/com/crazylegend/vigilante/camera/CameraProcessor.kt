package com.crazylegend.vigilante.camera

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.camera.camera2.internal.compat.CameraManagerCompat
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.coroutineScope
import com.crazylegend.common.ifTrue
import com.crazylegend.contextgetters.notificationManager
import com.crazylegend.coroutines.mainDispatcher
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.providers.UserNotificationsProvider
import com.crazylegend.vigilante.di.providers.prefs.camera.CameraPrefs
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.service.VigilanteService
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by crazy on 10/15/20 to long live and prosper !
 */
@ServiceScoped
@SuppressLint("RestrictedApi")
class CameraProcessor @Inject constructor(
        @ServiceContext private val context: Context,
        private val userNotificationsProvider: UserNotificationsProvider,
        private val cameraPrefs: CameraPrefs) : ServiceManagersCoroutines {

    private companion object {
        private const val cameraNotificationID = 69
    }

    private lateinit var manager: CameraManagerCompat

    //lifecycle
    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    //camera
    private lateinit var cameraCallback: CameraManager.AvailabilityCallback

    override fun initVars() {
        cameraCallback = cameraListener()
        manager = CameraManagerCompat.from(context)
    }

    override fun registerCallbacks() {
        manager.registerAvailabilityCallback(Executors.newSingleThreadExecutor(), cameraCallback)
    }

    override fun disposeResources() {
        manager.unregisterAvailabilityCallback(cameraCallback)
    }

    //region private
    private fun cameraListener(): CameraManager.AvailabilityCallback =
            object : CameraManager.AvailabilityCallback() {
                override fun onCameraAvailable(cameraId: String) {
                    super.onCameraAvailable(cameraId)
                    serviceLifecycleDispatcher.updateUI {
                        setCameraNotUsed()
                        stopNotificationIfUserEnabled()
                    }
                }

                override fun onCameraUnavailable(cameraId: String) {
                    super.onCameraUnavailable(cameraId)
                    serviceLifecycleDispatcher.updateUI {
                        setCameraUsed()
                    }
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
        cameraPrefs.areNotificationsEnabled.ifTrue {
            userNotificationsProvider.buildUsageNotification(cameraNotificationID, R.string.cam_being_used, cameraPrefs.getCameraNotificationLEDColorPref,
                    cameraPrefs.getCameraVibrationEffectPref, cameraPrefs.isBypassDNDEnabled)
        }
    }

    private inline fun ServiceLifecycleDispatcher.updateUI(crossinline function: () -> Unit) =
            lifecycle.coroutineScope.launch(mainDispatcher) {
                function()
            }

    private fun setCameraNotUsed() {
        VigilanteService.serviceLayoutListener?.hideCamera()
    }
    //endregion


}

