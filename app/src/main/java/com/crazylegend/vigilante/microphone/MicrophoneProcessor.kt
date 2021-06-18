package com.crazylegend.vigilante.microphone

import android.content.Context
import android.media.AudioManager
import android.media.AudioRecordingConfiguration
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.common.ifTrue
import com.crazylegend.contextgetters.audioManager
import com.crazylegend.contextgetters.notificationManager
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.providers.UserNotificationsProvider
import com.crazylegend.vigilante.di.providers.prefs.mic.MicrophonePrefs
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.service.VigilanteService
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/16/20 to long live and prosper !
 */
@ServiceScoped
class MicrophoneProcessor @Inject constructor(
        @ServiceContext private val context: Context,
        private val userNotificationsProvider: UserNotificationsProvider,
        private val microphonePrefs: MicrophonePrefs
) : ServiceManagersCoroutines {

    private companion object {
        private const val micNotificationID = 68
    }

    private lateinit var microphoneCallback: AudioManager.AudioRecordingCallback
    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    override fun initVars() {
        microphoneCallback = microphoneListener()
    }

    private fun microphoneListener(): AudioManager.AudioRecordingCallback =
            object : AudioManager.AudioRecordingCallback() {
                override fun onRecordingConfigChanged(configs: MutableList<AudioRecordingConfiguration>?) {
                    if (configs.isNullOrEmpty()) {
                        //mic isn't used
                        stopNotificationIfUserEnabled()
                        VigilanteService.serviceLayoutListener?.hideMic()
                    } else {
                        //mic is used
                        setMicrophoneIsUsed()
                        VigilanteService.serviceLayoutListener?.showMic()
                    }
                }
            }

    private fun sendNotificationIfUserEnabled() {
        microphonePrefs.areNotificationsEnabled.ifTrue {
            userNotificationsProvider.buildUsageNotification(micNotificationID, R.string.mic_being_used, microphonePrefs.getMicNotificationLEDColorPref,
                    microphonePrefs.getMicVibrationEffectPref, microphonePrefs.isBypassDNDEnabled)
        }
    }

    private fun stopNotificationIfUserEnabled() {
        context.notificationManager?.cancel(micNotificationID)
    }

    private fun setMicrophoneIsUsed() {
        sendNotificationIfUserEnabled()
    }


    override fun registerCallbacks() {
        context.audioManager.registerAudioRecordingCallback(microphoneCallback, null)
    }

    override fun disposeResources() {
        context.audioManager.unregisterAudioRecordingCallback(microphoneCallback)
    }
}