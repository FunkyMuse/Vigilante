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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        private const val COLOR_DOT_STATE = "colorState"
        private const val LAYOUT_STATE = "layoutState"
        private const val SIZE_STATE = "sizeState"
        private const val COLOR_NOTIFICATION_STATE = "colorNotificationState"
        private const val VIBRATION_STATE = "vibrationState"
        private const val SPACING_STATE = "customSpacingState"
    }

    private val args = CustomizationFragmentArgs.fromSavedStateHandle(savedStateHandle)
    private val prefBaseName get() = args.prefBaseName

    var dotColor = 0
        get() = savedStateHandle[COLOR_DOT_STATE] ?: when (prefBaseName) {
            CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraColorPref
            MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicColorPref
            LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationColorPref
            else -> throw throwMissingArgException()
        }
        set(value) {
            savedStateHandle[COLOR_DOT_STATE] = value
            field = value
        }

    var notificationLEDColor = 0
        get() = savedStateHandle[COLOR_NOTIFICATION_STATE] ?: dotColor
        set(value) {
            savedStateHandle[COLOR_NOTIFICATION_STATE] = value
            field = value
        }

    var size = 0.0f
        get() = savedStateHandle[SIZE_STATE] ?: when (prefBaseName) {
            CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraSizePref
            MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicSizePref
            LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationSizePref
            else -> throw throwMissingArgException()
        }
        set(value) {
            savedStateHandle[SIZE_STATE] = value
            field = value
        }

    var layoutPosition = 0
        get() = savedStateHandle[LAYOUT_STATE] ?: when (prefBaseName) {
            CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraPositionPref
            MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicPositionPref
            LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationPositionPref
            else -> throw throwMissingArgException()
        }
        set(value) {
            savedStateHandle[LAYOUT_STATE] = value
            field = value
        }


    var vibrationPosition = 0
        get() = savedStateHandle[VIBRATION_STATE] ?: when (prefBaseName) {
            CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraVibrationPositionPref
            MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicVibrationPositionPref
            LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationVibrationPositionPref
            else -> throw throwMissingArgException()
        }
        set(value) {
            savedStateHandle[VIBRATION_STATE] = value
            field = value
        }


    var spacing = 0
        get() = savedStateHandle[SPACING_STATE] ?: when (prefBaseName) {
            CAMERA_CUSTOMIZATION_BASE_PREF -> cameraPrefs.getCameraSpacing
            MIC_CUSTOMIZATION_BASE_PREF -> microphonePrefs.getMicSpacing
            LOCATION_CUSTOMIZATION_BASE_PREF -> locationPrefs.getLocationSpacing
            else -> throw throwMissingArgException()
        }
        set(value) {
            savedStateHandle[SPACING_STATE] = value
            field = value
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