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
import com.crazylegend.kotlinextensions.views.*
import com.crazylegend.vigilante.camera.CameraProcessor
import com.crazylegend.vigilante.databinding.ServiceLayoutDotBinding
import com.crazylegend.vigilante.di.providers.BroadcastProvider
import com.crazylegend.vigilante.di.providers.prefs.camera.CameraPrefs
import com.crazylegend.vigilante.di.providers.prefs.customization.CustomizationPrefs
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.di.providers.prefs.mic.MicrophonePrefs
import com.crazylegend.vigilante.location.LocationProcessor
import com.crazylegend.vigilante.microphone.MicrophoneProcessor
import com.crazylegend.vigilante.notifications.NotificationsProvider
import com.crazylegend.vigilante.permissions.PermissionsProcessor
import com.crazylegend.vigilante.settings.CAMERA_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.utils.DotPosition
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
    lateinit var prefsProvider: DefaultPreferencessProvider

    @Inject
    lateinit var microphonePrefs: MicrophonePrefs

    @Inject
    lateinit var cameraPrefs: CameraPrefs

    @Inject
    lateinit var customizationPrefs: CustomizationPrefs

    private lateinit var cameraParams: WindowManager.LayoutParams
    private lateinit var cameraBinding: ServiceLayoutDotBinding

    private lateinit var micParams: WindowManager.LayoutParams
    private lateinit var micBinding: ServiceLayoutDotBinding

    private val layoutCameraPositionPref get() = cameraPrefs.getCameraPositionPref
    private val layoutMicPositionPref get() = microphonePrefs.getMicPositionPref

    @SuppressLint("MissingPermission")
    override fun onServiceConnected() {
        setupCameraLayout()
        setupMicLayout()

        cameraProcessor.setServiceConnected()
        microphoneProcessor.setServiceConnected()
        broadcastProvider.setServiceConnected()
        notificationsProvider.setServiceConnected()
        permissionsProcessor.setServiceConnected()
        locationProcessor.setServiceConnected()

        serviceLayoutListener = object : ServiceLayoutListener {
            override fun showCamera() {
                if (::cameraBinding.isInitialized) {
                    cameraBinding.dot.visible()
                    checkIfTheyAreTheSameLayoutPositions()
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
                    checkIfTheyAreTheSameLayoutPositions()
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
                checkIfTheyAreTheSameLayoutPositions()
                windowManager?.updateViewLayout(cameraBinding.root, cameraParams)
            } else {
                updateMicPrefs()
                checkIfTheyAreTheSameLayoutPositions()
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
        if (microphonePrefs.isDotEnabled) {
            micParams = initParams(microphonePrefs.getLayoutMicPositionPref)
            micBinding = ServiceLayoutDotBinding.inflate(LayoutInflater.from(this))
            updateMicPrefs()
            addLayout(micBinding.root, micParams)
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

    private val defaultMargins get() = 5.dp

    private fun checkIfTheyAreTheSameLayoutPositions() {
        if (layoutCameraPositionPref == layoutMicPositionPref) {
            val customMargin = 20.dp
            val micVisibility = micBinding.dot.isVisible
            val camVisibility = cameraBinding.dot.isVisible
            if (micVisibility && camVisibility) {
                when (layoutCameraPositionPref) {
                    DotPosition.TOP_RIGHT.position -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginEnd = customMargin
                            marginStart = defaultMargins
                            bottomMargin = defaultMargins
                        }
                        resetCameraBindingMargins()
                    }
                    DotPosition.TOP_LEFT.position -> {
                        cameraBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = customMargin
                            marginEnd = defaultMargins
                            bottomMargin = defaultMargins
                        }
                        resetMicBindingMargins()
                    }
                    DotPosition.CENTER_RIGHT.position -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = defaultMargins
                            marginEnd = defaultMargins
                            bottomMargin = customMargin
                        }
                        resetCameraBindingMargins()
                    }
                    DotPosition.CENTER_LEFT.position -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = defaultMargins
                            marginEnd = defaultMargins
                            bottomMargin = customMargin
                        }
                        resetCameraBindingMargins()
                    }
                    DotPosition.BOTTOM_RIGHT.position -> {
                        micBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginEnd = customMargin
                            marginStart = defaultMargins
                            bottomMargin = defaultMargins
                        }
                        resetCameraBindingMargins()
                    }
                    DotPosition.BOTTOM_LEFT.position -> {
                        cameraBinding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
                            marginStart = customMargin
                            marginEnd = defaultMargins
                            bottomMargin = defaultMargins
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
                marginStart = defaultMargins
                bottomMargin = defaultMargins
                marginEnd = defaultMargins
            }
        }
    }

    private fun resetMicBindingMargins() {
        micBinding.dot.doOnLayout {
            it.updateLayoutParams<FrameLayout.LayoutParams> {
                marginStart = defaultMargins
                bottomMargin = defaultMargins
                marginEnd = defaultMargins
            }
        }
    }

    private fun updateMicPrefs() {
        if (::micBinding.isInitialized)
            updatePrefs(
                    micBinding,
                    microphonePrefs.getMicSizePref,
                    microphonePrefs.getMicColorPref,
                    microphonePrefs.getLayoutMicPositionPref,
                    micParams,
                    microphonePrefs.getMicSpacing.dp,
                    microphonePrefs.getMicPositionPref
            )
    }

    private fun updateCameraPrefs() {
        if (::cameraBinding.isInitialized)
            updatePrefs(
                    cameraBinding,
                    cameraPrefs.getCameraSizePref,
                    cameraPrefs.getCameraColorPref,
                    cameraPrefs.getLayoutCameraPositionPref,
                    cameraParams,
                    cameraPrefs.getCameraSpacing.dp,
                    cameraPrefs.getCameraPositionPref
            )
    }

    private fun updatePrefs(
        binding: ServiceLayoutDotBinding,
        sizePref: Float,
        colorPref: Int,
        positionPref: Int,
        params: WindowManager.LayoutParams,
        spacing: Int,
        gravityPosition: Int
    ) {

        binding.dot.setWidth(sizePref.toInt())
        binding.dot.setHeight(sizePref.toInt())
        binding.dot.setColorFilter(colorPref)
        params.gravity = positionPref

        when (gravityPosition) {
            DotPosition.TOP_RIGHT.position -> updateBindingParams(
                start = defaultMargins,
                end = spacing,
                top = spacing,
                bottom = defaultMargins,
                binding = binding
            )
            DotPosition.TOP_LEFT.position -> updateBindingParams(
                start = spacing,
                end = defaultMargins,
                top = spacing,
                bottom = defaultMargins,
                binding = binding
            )
            DotPosition.BOTTOM_RIGHT.position -> updateBindingParams(
                start = defaultMargins,
                end = spacing,
                top = defaultMargins,
                bottom = spacing,
                binding = binding
            )
            DotPosition.BOTTOM_LEFT.position -> updateBindingParams(
                start = spacing,
                end = defaultMargins,
                top = defaultMargins,
                bottom = spacing,
                binding = binding
            )
            else -> updateBindingParams(
                start = defaultMargins,
                end = defaultMargins,
                top = defaultMargins,
                bottom = defaultMargins,
                binding = binding
            )
        }
    }

    private fun updateBindingParams(
        start: Int,
        end: Int,
        top: Int,
        bottom: Int,
        binding: ServiceLayoutDotBinding
    ) {
        binding.dot.doOnLayout {
            it.updateLayoutParams<FrameLayout.LayoutParams> {
                marginStart = start
                bottomMargin = bottom
                marginEnd = end
                topMargin = top
            }
        }
    }

    private fun setupCameraLayout() {
        if (cameraPrefs.isDotEnabled) {
            cameraParams = initParams(cameraPrefs.getLayoutCameraPositionPref)
            cameraBinding = ServiceLayoutDotBinding.inflate(LayoutInflater.from(this))
            updateCameraPrefs()
            addLayout(cameraBinding.root, cameraParams)
            cameraBinding.dot.gone()
        }
    }

    private fun addLayout(root: FrameLayout, params: WindowManager.LayoutParams) {
        windowManager?.addView(root, params)
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

        if (::cameraBinding.isInitialized)
            windowManager?.removeView(cameraBinding.root)

        if (::micBinding.isInitialized)
            windowManager?.removeView(micBinding.root)

        currentPackageString = null
        super.onDestroy()
    }


}