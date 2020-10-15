package com.crazylegend.vigilante

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.crazylegend.vigilante.di.providers.CameraProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@AndroidEntryPoint
class VigilanteService : AccessibilityService() {

    @Inject
    lateinit var cameraProvider: CameraProvider


    override fun onServiceConnected() {
        cameraProvider.setServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        rememberEventPackageName(event)
    }

    override fun onCreate() {
        super.onCreate()
        cameraProvider.initLifecycle()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cameraProvider.onStart()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun rememberEventPackageName(event: AccessibilityEvent) {
        val eventPackageName = event.packageName
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && eventPackageName != null) {
            cameraProvider.eventAction(eventPackageName)
        }
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        cameraProvider.cleanUp()
        super.onDestroy()

    }

}