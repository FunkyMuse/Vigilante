package com.crazylegend.vigilante

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.location.LocationManager
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import com.crazylegend.kotlinextensions.context.inflater
import com.crazylegend.kotlinextensions.context.windowManager
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.vigilante.camera.CameraManager
import com.crazylegend.vigilante.clipboard.ClipboardManager
import com.crazylegend.vigilante.gps.GPSReceiver
import com.crazylegend.vigilante.microphone.MicrophoneManager
import com.crazylegend.vigilante.notifications.NotificationsProvider
import com.crazylegend.vigilante.permissions.PermissionsManager
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
    lateinit var cameraManager: CameraManager

    @Inject
    lateinit var microphoneManager: MicrophoneManager

    @Inject
    lateinit var clipboardManager: ClipboardManager

    @Inject
    lateinit var notificationsProvider: NotificationsProvider

    @Inject
    lateinit var permissionsManager: PermissionsManager

    private lateinit var outerFrame: FrameLayout
    private lateinit var outerFrameParams: WindowManager.LayoutParams

    private val currentPackage get() = currentPackageString ?: packageName

    private var denyButtonId: String? = null

    @SuppressLint("MissingPermission")
    override fun onServiceConnected() {
        cameraManager.setServiceConnected()
        microphoneManager.setServiceConnected()
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
        clipboardManager.processEvent(event)
        notificationsProvider.processEvent(event)
    }

    override fun onCreate() {
        super.onCreate()
        //region init
        cameraManager.initLifecycle()
        microphoneManager.initLifecycle()
        //endregion

        //region start
        cameraManager.onStart()
        microphoneManager.onStart()
        //endregion
    }

    private fun rememberEventPackageName(event: AccessibilityEvent) {
        val eventPackageName = event.packageName
        currentPackageString = eventPackageName?.toString() ?: packageName
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && eventPackageName != null) {
            cameraManager.eventActionByPackageName(eventPackageName)
            microphoneManager.eventActionByPackageName(eventPackageName)
            //logViewHierarchy(event.source)
            extractPermission(event.source)
        }
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            val buttonId = denyButtonId
            if (buttonId != null && event.source.viewIdResourceName != null && event.source.viewIdResourceName == buttonId) {
                debug { "CLICKED DENY ${event.source.text}" }
                denyButtonId = null
            }
        }
    }

    private fun extractPermission(nodeInfo: AccessibilityNodeInfo?, depth: Int = 0) {
        if (nodeInfo == null) return
        //Log the info you care about here... I choce classname and view resource name, because they are simple, but interesting.
        val viewIdResource = nodeInfo.viewIdResourceName
        if (viewIdResource != null && viewIdResource.contains("com.android.permissioncontroller", true)) {
            debug { "WAT ${nodeInfo.text} ${nodeInfo.viewIdResourceName}" }
            if (viewIdResource == "com.android.permissioncontroller:id/deny_radio_button") {
                denyButtonId = "com.android.permissioncontroller:id/deny_radio_button"
            }
        }
        for (i in 0 until nodeInfo.childCount) {
            extractPermission(nodeInfo.getChild(i), depth + 1)
        }
    }

    private fun logViewHierarchy(nodeInfo: AccessibilityNodeInfo?, depth: Int = 0) {
        if (nodeInfo == null) return
        var spacerString = ""
        for (i in 0 until depth) {
            spacerString += '-'
        }
        //Log the info you care about here... I choce classname and view resource name, because they are simple, but interesting.
        Log.d("TAG", spacerString + nodeInfo.className + " " + nodeInfo.viewIdResourceName)
        for (i in 0 until nodeInfo.childCount) {
            logViewHierarchy(nodeInfo.getChild(i), depth + 1)
        }
    }


    override fun onInterrupt() {}

    override fun onDestroy() {
        cameraManager.cleanUp()
        microphoneManager.cleanUp()
        unregisterReceiver(gpsReceiver)
        windowManager?.removeView(outerFrame)
        currentPackageString = null
        super.onDestroy()
    }

}