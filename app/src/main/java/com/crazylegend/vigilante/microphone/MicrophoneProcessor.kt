package com.crazylegend.vigilante.microphone

import android.content.Context
import android.media.AudioManager
import android.media.AudioRecordingConfiguration
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.kotlinextensions.context.audioManager
import com.crazylegend.kotlinextensions.context.notificationManager
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.di.providers.UserNotificationsProvider
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
        private val prefsProvider: PrefsProvider) : ServiceManagersCoroutines {

    private lateinit var microphoneCallback: AudioManager.AudioRecordingCallback
    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)
    private val micNotificationID = 68

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
        prefsProvider.areNotificationsEnabled.ifTrue {
            userNotificationsProvider.buildUsageNotification(micNotificationID, R.string.mic_being_used, prefsProvider.getMicNotificationLEDColorPref,
                    prefsProvider.getMicVibrationEffectPref)
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