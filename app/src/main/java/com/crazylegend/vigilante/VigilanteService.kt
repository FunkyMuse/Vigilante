package com.crazylegend.vigilante

import android.accessibilityservice.AccessibilityService
import android.content.ClipboardManager
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.crazylegend.kotlinextensions.context.clipboardManager
import com.crazylegend.kotlinextensions.context.getTextFromClipboard
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.vigilante.di.providers.CameraProvider
import com.crazylegend.vigilante.di.providers.MicrophoneProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@AndroidEntryPoint
class VigilanteService : AccessibilityService() {

    @Inject
    lateinit var cameraProvider: CameraProvider

    @Inject
    lateinit var microphoneProvider: MicrophoneProvider

    override fun onServiceConnected() {
        cameraProvider.setServiceConnected()
        microphoneProvider.setServiceConnected()
    }

    private var packageNameUsingClipboard: String? = null
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        rememberEventPackageName(event)
    }

    private val clipboardListener = ClipboardManager.OnPrimaryClipChangedListener {
        debug {
            "CLIPBOARD CHANGE NOTIFIED" +
                    "\nUSED BY $packageNameUsingClipboard" +
                    "\nHAS CLIP ${clipboardManager?.hasPrimaryClip()}" +
                    "\nTEXT $getTextFromClipboard"
        }
    }

    override fun onCreate() {
        super.onCreate()
        cameraProvider.initLifecycle()
        microphoneProvider.initLifecycle()
        clipboardManager?.addPrimaryClipChangedListener(clipboardListener)
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
            packageNameUsingClipboard = eventPackageName.toString()
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        cameraProvider.cleanUp()
        microphoneProvider.cleanUp()
        clipboardManager?.removePrimaryClipChangedListener(clipboardListener)
        super.onDestroy()
    }

}