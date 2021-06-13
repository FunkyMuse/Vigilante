package com.crazylegend.vigilante.customization

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.providers.prefs.camera.CameraPrefs
import com.crazylegend.vigilante.di.providers.prefs.location.LocationPrefs
import com.crazylegend.vigilante.di.providers.prefs.mic.MicrophonePrefs
import com.crazylegend.vigilante.settings.CAMERA_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.settings.LOCATION_CUSTOMIZATION_BASE_PREF
import com.crazylegend.vigilante.settings.MIC_CUSTOMIZATION_BASE_PREF
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by funkymuse on 6/13/21 to long live and prosper !
 */
@HiltViewModel
class CustomizationViewModel @Inject constructor(
        private val cameraPrefs: CameraPrefs,
        private val locationPrefs: LocationPrefs,
        private val microphonePrefs: MicrophonePrefs,
        private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val prefBaseName get() = savedStateHandle.get<String>("prefBaseName")

    val defaultDotColor
        get() =
            when (prefBaseName) {
                CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraColorPref
                MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicColorPref
                LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationColorPref
                else -> throw throwMissingArgException()
            }

    val defaultNotificationLEDColor
        get() = defaultDotColor

    val defaultSize
        get() =
            when (prefBaseName) {
                CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraSizePref
                MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicSizePref
                LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationSizePref
                else -> throw throwMissingArgException()
            }

    val defaultLayoutPosition
        get() =
            when (prefBaseName) {
                CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraPositionPref
                MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicPositionPref
                LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationPositionPref
                else -> throw throwMissingArgException()
            }


    val defaultVibrationPosition
        get() =
            when (prefBaseName) {
                CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraVibrationPositionPref
                MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicVibrationPositionPref
                LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationVibrationPositionPref
                else -> throw throwMissingArgException()
            }


    val spacing
        get() =
            when (prefBaseName) {
                CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraSpacing
                MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicSpacing
                LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationSpacing
                else -> throw throwMissingArgException()
            }


    val title
        @StringRes get() =
            when (prefBaseName) {
                CAMERA_CUSTOMIZATION_BASE_PREF -> R.string.camera_title
                MIC_CUSTOMIZATION_BASE_PREF -> R.string.microphone_title
                LOCATION_CUSTOMIZATION_BASE_PREF -> R.string.location_title
                else -> throw throwMissingArgException()
            }


    private fun throwMissingArgException(): IllegalStateException {
        val exception = IllegalStateException("Argument is missing in customization")
        CrashyReporter.logException(exception)
        return exception
    }
}