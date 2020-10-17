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
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.vigilante.di.providers.CameraProvider
import com.crazylegend.vigilante.di.providers.MicrophoneProvider
import com.crazylegend.vigilante.gps.GPSReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@AndroidEntryPoint
class VigilanteService : AccessibilityService() {

    private lateinit var gpsReceiver: GPSReceiver

    @Inject
    lateinit var cameraProvider: CameraProvider

    @Inject
    lateinit var microphoneProvider: MicrophoneProvider

    private lateinit var outerFrame: FrameLayout
    private lateinit var outerFrameParams: WindowManager.LayoutParams


    @SuppressLint("MissingPermission")
    override fun onServiceConnected() {
        cameraProvider.setServiceConnected()
        microphoneProvider.setServiceConnected()
        gpsReceiver = GPSReceiver()

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

        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        registerReceiver(gpsReceiver, filter)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        event.toString()

        rememberEventPackageName(event)
        debug { "$event" }
        /*debug {
                "isEditable ${event.source?.isEditable?.toString()}\n" +
                "text ${event.source?.text?.toString()}\n" +
                "text ${event.source?.text?.toString()}\n" +
                "packageName ${event.source?.packageName?.toString()}\n"
        }*/

        val hasClickedCopy = (event.contentDescription != null && event.contentDescription.toString().toLowerCase(Locale.getDefault()).equals("copy", true))
        val hasClickedPaste = (event.contentDescription != null && event.contentDescription.toString().toLowerCase(Locale.getDefault()).equals("paste", true))
        val text = event.text?.toString()
        debug { "IS CLICKED COPY $hasClickedCopy for ${text}" }
        debug { "IS CLICKED PASTE $hasClickedPaste for ${text}" }
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            val selectedText = tryOrNull { event.text?.toString()?.substring(event.fromIndex, event.toIndex + 1)?.toString() }
            debug { "SELECTED THE TEXT $selectedText" }
        }
    }

    private fun printEventType(eventType: Int) {
        val textType = when (eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "TYPE_WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "TYPE_WINDOW_CONTENT_CHANGED"
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> "TYPE_WINDOWS_CHANGED"
            AccessibilityEvent.TYPE_ANNOUNCEMENT -> "TYPE_ANNOUNCEMENT"
            AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT -> "TYPE_ASSIST_READING_CONTEXT"
            AccessibilityEvent.TYPE_GESTURE_DETECTION_END -> "TYPE_GESTURE_DETECTION_END"
            AccessibilityEvent.TYPE_GESTURE_DETECTION_START -> "TYPE_GESTURE_DETECTION_START"
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> "TYPE_NOTIFICATION_STATE_CHANGED"
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END -> "TYPE_TOUCH_EXPLORATION_GESTURE_END"
            AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START -> "TYPE_TOUCH_EXPLORATION_GESTURE_START"
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> "TYPE_TOUCH_INTERACTION_END"
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> "TYPE_TOUCH_INTERACTION_START"
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED -> "TYPE_VIEW_ACCESSIBILITY_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED"
            AccessibilityEvent.TYPE_VIEW_CLICKED -> "TYPE_VIEW_CLICKED"
            AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED -> "TYPE_VIEW_CONTEXT_CLICKED"
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> "TYPE_VIEW_FOCUSED"
            AccessibilityEvent.TYPE_VIEW_HOVER_ENTER -> "TYPE_VIEW_HOVER_ENTER"
            AccessibilityEvent.TYPE_VIEW_HOVER_EXIT -> "TYPE_VIEW_HOVER_EXIT"
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> "TYPE_VIEW_LONG_CLICKED"
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> "TYPE_VIEW_SCROLLED"
            AccessibilityEvent.TYPE_VIEW_SELECTED -> "TYPE_VIEW_SELECTED"
            AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY -> "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY"
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> "TYPE_VIEW_TEXT_CHANGED"
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> "TYPE_VIEW_TEXT_SELECTION_CHANGED"
            else -> ""
        }
        debug { "EVENT $textType" }
    }

    override fun onCreate() {
        super.onCreate()
        cameraProvider.initLifecycle()
        microphoneProvider.initLifecycle()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cameraProvider.onStart()
        microphoneProvider.onStart()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun rememberEventPackageName(event: AccessibilityEvent) {
        val eventPackageName = event.packageName
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && eventPackageName != null) {
            cameraProvider.eventAction(eventPackageName)
            microphoneProvider.eventAction(eventPackageName)
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        cameraProvider.cleanUp()
        microphoneProvider.cleanUp()
        unregisterReceiver(gpsReceiver)
        super.onDestroy()
    }

}