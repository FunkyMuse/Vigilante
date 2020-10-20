package com.crazylegend.vigilante

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.location.LocationManager
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import com.crazylegend.kotlinextensions.context.inflater
import com.crazylegend.kotlinextensions.context.windowManager
import com.crazylegend.vigilante.camera.CameraProvider
import com.crazylegend.vigilante.clipboard.ClipboardProvider
import com.crazylegend.vigilante.gps.GPSReceiver
import com.crazylegend.vigilante.microphone.MicrophoneProvider
import com.crazylegend.vigilante.notifications.NotificationsProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@AndroidEntryPoint
class VigilanteService : AccessibilityService() {

    companion object {
        var currentPackageString: String? = null
    }

    private lateinit var gpsReceiver: GPSReceiver

    @Inject
    lateinit var cameraProvider: CameraProvider

    @Inject
    lateinit var microphoneProvider: MicrophoneProvider

    @Inject
    lateinit var clipboardProvider: ClipboardProvider

    @Inject
    lateinit var notificationsProvider: NotificationsProvider

    private lateinit var outerFrame: FrameLayout
    private lateinit var outerFrameParams: WindowManager.LayoutParams

    private val currentPackage get() = currentPackageString ?: packageName

    @SuppressLint("MissingPermission")
    override fun onServiceConnected() {
        cameraProvider.setServiceConnected()
        microphoneProvider.setServiceConnected()
        gpsReceiver = GPSReceiver()

        setupHoverLayout()

        registerGPSReceiver()
    }

    private fun setupHoverLayout() {
        outerFrame = FrameLayout(this)
        outerFrameParams = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP or Gravity.START
        }
        inflater.inflate(R.layout.outer_frame, outerFrame)
        windowManager?.addView(outerFrame, outerFrameParams)
    }

    private fun registerGPSReceiver() {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        registerReceiver(gpsReceiver, filter)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        rememberEventPackageName(event)
        clipboardProvider.processEvent(event)
        notificationsProvider.processEvent(event)
    }

    override fun onCreate() {
        super.onCreate()
        //region init
        cameraProvider.initLifecycle()
        microphoneProvider.initLifecycle()
        //endregion

        //region start
        cameraProvider.onStart()
        microphoneProvider.onStart()
        //endregion
    }

    private fun rememberEventPackageName(event: AccessibilityEvent) {
        val eventPackageName = event.packageName
        currentPackageString = eventPackageName?.toString() ?: packageName
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && eventPackageName != null) {
            cameraProvider.eventActionByPackageName(eventPackageName)
            microphoneProvider.eventActionByPackageName(eventPackageName)
        }
    }


    override fun onInterrupt() {}

    override fun onDestroy() {
        cameraProvider.cleanUp()
        microphoneProvider.cleanUp()
        unregisterReceiver(gpsReceiver)
        windowManager?.removeView(outerFrame)
        currentPackageString = null
        super.onDestroy()
    }

}