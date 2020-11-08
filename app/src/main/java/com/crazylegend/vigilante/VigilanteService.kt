package com.crazylegend.vigilante

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import com.crazylegend.kotlinextensions.context.inflater
import com.crazylegend.kotlinextensions.context.windowManager
import com.crazylegend.vigilante.camera.CameraProcessor

import com.crazylegend.vigilante.di.providers.BroadcastProvider
import com.crazylegend.vigilante.microphone.MicrophoneProcessor
import com.crazylegend.vigilante.notifications.NotificationsProvider
import com.crazylegend.vigilante.permissions.PermissionsProcessor
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
    }

    @Inject
    lateinit var broadcastProvider: BroadcastProvider

    @Inject
    lateinit var cameraProcessor: CameraProcessor

    @Inject
    lateinit var microphoneProcessor: MicrophoneProcessor

    @Inject
    lateinit var notificationsProvider: NotificationsProvider

    @Inject
    lateinit var permissionsProcessor: PermissionsProcessor


    private lateinit var outerFrame: FrameLayout
    private lateinit var outerFrameParams: WindowManager.LayoutParams

    @SuppressLint("MissingPermission")
    override fun onServiceConnected() {
        cameraProcessor.setServiceConnected()
        microphoneProcessor.setServiceConnected()
        broadcastProvider.setServiceConnected()
        notificationsProvider.setServiceConnected()
        permissionsProcessor.setServiceConnected()

        //setupHoverLayout()
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
        //endregion

        //region start
        cameraProcessor.onStart()
        microphoneProcessor.onStart()
        broadcastProvider.onStart()
        notificationsProvider.onStart()
        permissionsProcessor.onStart()
        //endregion
    }

    private fun rememberEventPackageName(event: AccessibilityEvent) {
        val eventPackageName = event.packageName
        currentPackageString = eventPackageName?.toString() ?: packageName
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && eventPackageName != null && eventPackageName !in dismissPackages) {
            cameraProcessor.eventActionByPackageName(eventPackageName)
            permissionsProcessor.eventActionByPackageName(eventPackageName)
            microphoneProcessor.eventActionByPackageName(eventPackageName)
            //logViewHierarchy(event.source)
            permissionsProcessor.extractPermissionMessage(event.source)
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
        cameraProcessor.cleanUp()
        microphoneProcessor.cleanUp()
        broadcastProvider.cleanUp()
        notificationsProvider.cleanUp()
        permissionsProcessor.cleanUp()
        //windowManager?.removeView(outerFrame)
        currentPackageString = null
        super.onDestroy()
    }

}