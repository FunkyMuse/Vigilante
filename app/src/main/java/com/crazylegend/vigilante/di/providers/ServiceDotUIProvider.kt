package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import com.crazylegend.view.setHeight
import com.crazylegend.view.setWidth
import com.crazylegend.vigilante.databinding.ServiceLayoutDotBinding
import com.crazylegend.vigilante.di.providers.prefs.camera.CameraPrefs
import com.crazylegend.vigilante.di.providers.prefs.location.LocationPrefs
import com.crazylegend.vigilante.di.providers.prefs.mic.MicrophonePrefs
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by funkymuse on 6/13/21 to long live and prosper !
 */
@ServiceScoped
class ServiceDotUIProvider @Inject constructor(@ServiceContext private val context: Context,
                                               private val cameraPrefs: CameraPrefs,
                                               private val microphonePrefs: MicrophonePrefs,
                                               private val locationPrefs: LocationPrefs) {
    private val serviceLayoutDotBinding get() = ServiceLayoutDotBinding.inflate(LayoutInflater.from(context))

    lateinit var cameraBinding: ServiceLayoutDotBinding
        private set
    private lateinit var cameraParams: WindowManager.LayoutParams


    lateinit var micBinding: ServiceLayoutDotBinding
        private set
    private lateinit var micParams: WindowManager.LayoutParams


    lateinit var locationBinding: ServiceLayoutDotBinding
        private set
    private lateinit var locationParams: WindowManager.LayoutParams

    fun initViews() {
        cameraBinding = serviceLayoutDotBinding
        cameraParams = initParams()
        updateCameraPrefs()

        micBinding = serviceLayoutDotBinding
        micParams = initParams()
        updateMicPrefs()

        locationBinding = serviceLayoutDotBinding
        locationParams = initParams()
        updateLocationPrefs()
    }

    fun updateCameraPrefs() {
        updatePrefs(cameraBinding, cameraPrefs.getCameraSizePref, cameraPrefs.getCameraColorPref, cameraPrefs.getCameraSpacing)
    }

    fun updateMicPrefs() {
        updatePrefs(micBinding, microphonePrefs.getMicSizePref, microphonePrefs.getMicColorPref, microphonePrefs.getMicSpacing)
    }

    fun updateLocationPrefs() {
        updatePrefs(locationBinding, locationPrefs.getLocationSizePref, locationPrefs.getLocationColorPref, locationPrefs.getLocationSpacing)
    }

    private fun initParams(): WindowManager.LayoutParams =
            WindowManager.LayoutParams().apply {
                type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
                format = PixelFormat.TRANSLUCENT
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
            }


    private fun updatePrefs(binding: ServiceLayoutDotBinding, sizePref: Float, colorPref: Int, spacing: Int) {
        binding.dot.setWidth(sizePref.toInt())
        binding.dot.setHeight(sizePref.toInt())
        binding.dot.setColorFilter(colorPref)
        binding.dot.updateLayoutParams<FrameLayout.LayoutParams> {
            setMargins(spacing)
        }
    }

}