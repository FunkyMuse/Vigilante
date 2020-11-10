package com.crazylegend.vigilante.service

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import androidx.core.view.doOnLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.crazylegend.kotlinextensions.context.windowManager
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setHeight
import com.crazylegend.kotlinextensions.views.setWidth
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.vigilante.camera.CameraProcessor
import com.crazylegend.vigilante.databinding.ServiceLayoutDotBinding
import com.crazylegend.vigilante.di.providers.BroadcastProvider
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.microphone.MicrophoneProcessor
import com.crazylegend.vigilante.notifications.NotificationsProvider
import com.crazylegend.vigilante.permissions.PermissionsProcessor
import com.crazylegend.vigilante.settings.CAMERA_CUSTOMIZATION_BASE_PREF
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
    lateinit var microphoneProcessor: MicrophoneProcessor

    @Inject
    lateinit var notificationsProvider: NotificationsProvider

    @Inject
    lateinit var permissionsProcessor: PermissionsProcessor

    @Inject
    lateinit var prefsProvider: PrefsProvider

    private lateinit var cameraParams: WindowManager.LayoutParams
    private lateinit var cameraBinding: ServiceLayoutDotBinding

    private lateinit var micParams: WindowManager.LayoutParams
    private lateinit var micBinding: ServiceLayoutDotBinding


    @SuppressLint("MissingPermission")
    override fun onServiceConnected() {
        setupCameraLayout()
        setupMicLayout()

        cameraProcessor.setServiceConnected()
        microphoneProcessor.setServiceConnected()
        broadcastProvider.setServiceConnected()
        notificationsProvider.setServiceConnected()
        permissionsProcessor.setServiceConnected()

        serviceLayoutListener = object : ServiceLayoutListener {
            override fun showCamera() {
                if (::cameraBinding.isInitialized) {
                    cameraBinding.dot.visible()
                    checkIfTheyAreTheSameLayoutPositions(prefsProvider.getMicPositionPref, prefsProvider.getCameraPositionPref)
                }
            }

            override fun hideCamera() {
                if (::cameraBinding.isInitialized) {
                    cameraBinding.dot.gone()
                    resetMargins()
                }
            }

            override fun showMic() {
                if (::micBinding.isInitialized) {
                    micBinding.dot.visible()
                    checkIfTheyAreTheSameLayoutPositions(prefsProvider.getMicPositionPref, prefsProvider.getCameraPositionPref)
                }
            }

            override fun hideMic() {
                if (::micBinding.isInitialized) {
                    micBinding.dot.gone()
                    resetMargins()
                }
            }
        }

        serviceParamsListener = ServiceParamsListener {
            if (it == CAMERA_CUSTOMIZATION_BASE_PREF) {
                updateCameraPrefs()
                checkIfTheyAreTheSameLayoutPositions(prefsProvider.getMicPositionPref, prefsProvider.getCameraPositionPref)
                windowManager?.updateViewLayout(cameraBinding.root, cameraParams)
            } else {
                updateMicPrefs()
                checkIfTheyAreTheSameLayoutPositions(prefsProvider.getMicPositionPref, prefsProvider.getCameraPositionPref)
                windowManager?.updateViewLayout(micBinding.root, micParams)
            }
        }
    }

    private fun resetMargins() {
        if (micBinding.dot.isVisible && cameraBinding.dot.isGone) {
            resetMicBindingMargins()
        }
        if (cameraBinding.dot.isVisible && micBinding.dot.isGone) {
            resetCameraBindingMargins()
        }
    }

    private fun setupMicLayout() {
        if (prefsProvider.isDotEnabled) {
            micParams = initParams(prefsProvider.getLayoutMicPositionPref)
            micBinding = ServiceLayoutDotBinding.inflate(LayoutInflater.from(this))
            updateMicPrefs()
            windowManager?.addView(micBinding.root, micParams)
            micBinding.dot.gone()
        }
    }

    private fun initParams(positionPref: Int): WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = positionPref
        }
    }

    private fun checkIfTheyAreTheSameLayoutPositions(layoutMicPositionPref: Int, layoutCameraPositionPref: Int) {
        if (layoutCameraPositionPref == layoutMicPositionPref) {
            val customMargin = 100
            val micVisibility = micBinding.dot.isVisible
            val camVisibility = cameraBinding.dot.isVisible
            if (micVisibility && camVisibility) {
                when (layoutCameraPositionPref) {
                    0 -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = customMargin
                            marginEnd = 0
                            bottomMargin = 0
                        }
                        resetCameraBindingMargins()
                    }
                    1 -> {
                        cameraBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginEnd = customMargin
                            marginStart = 0
                            bottomMargin = 0
                        }
                        resetMicBindingMargins()
                    }
                    2 -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = 0
                            marginEnd = 0
                            bottomMargin = customMargin
                        }
                        resetCameraBindingMargins()
                    }
                    3 -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = 0
                            marginEnd = 0
                            bottomMargin = customMargin
                        }
                        resetCameraBindingMargins()
                    }
                    4 -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = customMargin
                            marginEnd = 0
                            bottomMargin = 0
                        }
                        resetCameraBindingMargins()
                    }
                    5 -> {
                        cameraBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginEnd = customMargin
                            marginStart = 0
                            bottomMargin = 0
                        }
                        resetMicBindingMargins()
                    }
                }
            }
        }
    }

    private fun resetCameraBindingMargins() {
        cameraBinding.dot.doOnLayout {
            it.updateLayoutParams<FrameLayout.LayoutParams> {
                marginStart = 0
                bottomMargin = 0
                marginEnd = 0
            }
        }
    }

    private fun resetMicBindingMargins() {
        micBinding.dot.doOnLayout {
            it.updateLayoutParams<FrameLayout.LayoutParams> {
                marginStart = 0
                bottomMargin = 0
                marginEnd = 0
            }
        }
    }

    private fun updateMicPrefs() {
        if (::micBinding.isInitialized)
            updatePrefs(micBinding, prefsProvider.getMicSizePref, prefsProvider.getMicColorPref, prefsProvider.getLayoutMicPositionPref, micParams)
    }

    private fun updateCameraPrefs() {
        if (::cameraBinding.isInitialized)
            updatePrefs(cameraBinding, prefsProvider.getCameraSizePref, prefsProvider.getCameraColorPref, prefsProvider.getLayoutCameraPositionPref, cameraParams)
    }

    private fun updatePrefs(binding: ServiceLayoutDotBinding, sizePref: Float, colorPref: Int, positionPref: Int, params: WindowManager.LayoutParams) {
        binding.dot.setWidth(sizePref.toInt())
        binding.dot.setHeight(sizePref.toInt())
        binding.dot.setColorFilter(colorPref)
        params.gravity = positionPref
    }

    private fun setupCameraLayout() {
        if (prefsProvider.isDotEnabled) {
            cameraParams = initParams(prefsProvider.getLayoutCameraPositionPref)
            cameraBinding = ServiceLayoutDotBinding.inflate(LayoutInflater.from(this))
            updateCameraPrefs()
            windowManager?.addView(cameraBinding.root, cameraParams)
            cameraBinding.dot.gone()
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

        if (::cameraBinding.isInitialized)
            windowManager?.removeView(cameraBinding.root)

        if (::micBinding.isInitialized)
            windowManager?.removeView(micBinding.root)

        currentPackageString = null
        super.onDestroy()
    }

}