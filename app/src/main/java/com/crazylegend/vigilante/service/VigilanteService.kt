package com.crazylegend.vigilante.service

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.crazylegend.vigilante.camera.CameraProcessor
import com.crazylegend.vigilante.di.providers.BroadcastProvider
import com.crazylegend.vigilante.di.providers.ServiceDotUIProvider
import com.crazylegend.vigilante.di.providers.ServiceUIProvider
import com.crazylegend.vigilante.di.providers.prefs.camera.CameraPrefs
import com.crazylegend.vigilante.di.providers.prefs.location.LocationPrefs
import com.crazylegend.vigilante.di.providers.prefs.mic.MicrophonePrefs
import com.crazylegend.vigilante.location.LocationProcessor
import com.crazylegend.vigilante.microphone.MicrophoneProcessor
import com.crazylegend.vigilante.notifications.NotificationsProvider
import com.crazylegend.vigilante.permissions.PermissionsProcessor
import com.crazylegend.vigilante.settings.CAMERA_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.settings.LOCATION_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.settings.MIC_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.utils.dismissPackages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@AndroidEntryPoint
class VigilanteService : AccessibilityService() {

    companion object {
        var currentPackageString: String? = null
        var serviceLayoutListener: ServiceLayoutListener? = null
        var serviceParamsListener: ServiceParamsListener? = null
    }

    @Inject
    lateinit var broadcastProvider: BroadcastProvider

    @Inject
    lateinit var cameraProcessor: CameraProcessor

    @Inject
    lateinit var locationProcessor: LocationProcessor

    @Inject
    lateinit var microphoneProcessor: MicrophoneProcessor

    @Inject
    lateinit var notificationsProvider: NotificationsProvider

    @Inject
    lateinit var permissionsProcessor: PermissionsProcessor

    @Inject
    lateinit var microphonePrefs: MicrophonePrefs

    @Inject
    lateinit var cameraPrefs: CameraPrefs

    @Inject
    lateinit var locationPrefs: LocationPrefs

    @Inject
    lateinit var serviceUIProvider: ServiceUIProvider

    @Inject
    lateinit var serviceDotUIProvider: ServiceDotUIProvider


    private val layoutCameraPositionPref get() = cameraPrefs.getCameraPositionPref
    private val layoutMicPositionPref get() = microphonePrefs.getMicPositionPref
    private val layoutLocationPositionPref get() = locationPrefs.getLocationPositionPref

    @SuppressLint("MissingPermission")
    override fun onServiceConnected() {
        serviceUIProvider.initViews()
        serviceDotUIProvider.initViews()

        cameraProcessor.setServiceConnected()
        microphoneProcessor.setServiceConnected()
        broadcastProvider.setServiceConnected()
        notificationsProvider.setServiceConnected()
        permissionsProcessor.setServiceConnected()
        locationProcessor.setServiceConnected()

        serviceLayoutListener = object : ServiceLayoutListener {
            override fun showCamera() {
                if (cameraPrefs.isDotEnabled)
                    serviceUIProvider.addDot(serviceDotUIProvider.cameraBinding.root, layoutCameraPositionPref, CAMERA_CUSTOMIZATION_BASE_PREF)
            }

            override fun hideCamera() {
                serviceUIProvider.removeDot(layoutCameraPositionPref, CAMERA_CUSTOMIZATION_BASE_PREF)
            }

            override fun showMic() {
                if (microphonePrefs.isDotEnabled)
                    serviceUIProvider.addDot(serviceDotUIProvider.micBinding.root, layoutMicPositionPref, MIC_CUSTOMIZATION_BASE_PREF)
            }

            override fun hideMic() {
                serviceUIProvider.removeDot(layoutMicPositionPref, MIC_CUSTOMIZATION_BASE_PREF)
            }

            override fun showLocation() {
                if (locationPrefs.isDotEnabled)
                    serviceUIProvider.addDot(serviceDotUIProvider.locationBinding.root, layoutLocationPositionPref, LOCATION_CUSTOMIZATION_BASE_PREF)
            }

            override fun hideLocation() {
                serviceUIProvider.removeDot(layoutLocationPositionPref, LOCATION_CUSTOMIZATION_BASE_PREF)
            }
        }

        serviceParamsListener = ServiceParamsListener {
            when (it) {
                CAMERA_CUSTOMIZATION_BASE_PREF -> {
                    serviceDotUIProvider.updateCameraPrefs()
                    serviceUIProvider.updateDot(layoutCameraPositionPref)
                }
                MIC_CUSTOMIZATION_BASE_PREF -> {
                    serviceDotUIProvider.updateMicPrefs()
                    serviceUIProvider.updateDot(layoutMicPositionPref)
                }
                LOCATION_CUSTOMIZATION_BASE_PREF -> {
                    serviceDotUIProvider.updateLocationPrefs()
                    serviceUIProvider.updateDot(layoutLocationPositionPref)
                }
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        rememberEventPackageName(event)
        notificationsProvider.processEvent(event)
    }

    override fun onCreate() {
        super.onCreate()
        //region init
        cameraProcessor.initLifecycle()
        microphoneProcessor.initLifecycle()
        broadcastProvider.initLifecycle()
        notificationsProvider.initLifecycle()
        permissionsProcessor.initLifecycle()
        locationProcessor.initLifecycle()
        //endregion

        //region start
        cameraProcessor.onStart()
        microphoneProcessor.onStart()
        broadcastProvider.onStart()
        notificationsProvider.onStart()
        permissionsProcessor.onStart()
        locationProcessor.onStart()
        //endregion
    }

    private fun rememberEventPackageName(event: AccessibilityEvent) {
        val eventPackageName = event.packageName
        currentPackageString = eventPackageName?.toString() ?: packageName
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && eventPackageName != null) {
            if (eventPackageName !in dismissPackages) {
                permissionsProcessor.eventActionByPackageName(eventPackageName)
            }
            permissionsProcessor.extractPermissionMessage(event.source)
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        cameraProcessor.cleanUp()
        microphoneProcessor.cleanUp()
        broadcastProvider.cleanUp()
        notificationsProvider.cleanUp()
        permissionsProcessor.cleanUp()
        locationProcessor.cleanUp()

        serviceUIProvider.cleanUp()

        currentPackageString = null
        super.onDestroy()
    }


}