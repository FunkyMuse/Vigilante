package com.crazylegend.vigilante.microphone

import android.content.Context
import android.media.AudioManager
import android.media.AudioRecordingConfiguration
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.kotlinextensions.context.audioManager
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/16/20 to long live and prosper !
 */
@ServiceScoped
class MicrophoneProcessor @Inject constructor(@ServiceContext private val context: Context) : ServiceManagersCoroutines {

    private lateinit var microphoneCallback: AudioManager.AudioRecordingCallback
    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    private var packageUsingMicrophone: String? = null
    private var wasMicrophoneBeingUsed = false
    private var microphoneStartedUsageTime: Long? = null

    override fun initVars() {
        microphoneCallback = microphoneListener()
    }

    private fun microphoneListener(): AudioManager.AudioRecordingCallback =
            object : AudioManager.AudioRecordingCallback() {
                override fun onRecordingConfigChanged(configs: MutableList<AudioRecordingConfiguration>?) {
                    if (configs.isNullOrEmpty()) {
                        //mic isn't used
                        setMicrophoneIsNotUsed()
                    } else {
                        //mic is used
                        setMicrophoneIsUsed()
                    }
                }
            }

    private fun setMicrophoneIsNotUsed() {
        wasMicrophoneBeingUsed = false
        debug { "PACKAGE NOT USING MICROPHONE $packageUsingMicrophone" }
        val microphoneEndedUsedAtTime = currentTimeMillis
        microphoneStartedUsageTime = null
        packageUsingMicrophone = null
    }

    private fun setMicrophoneIsUsed() {
        wasMicrophoneBeingUsed = true
        microphoneStartedUsageTime = currentTimeMillis
        debug { "PACKAGE USING MICROPHONE $packageUsingMicrophone" }
    }


    override fun registerCallbacks() {
        context.audioManager.registerAudioRecordingCallback(microphoneCallback, null)
    }

    override fun disposeResources() {
        context.audioManager.unregisterAudioRecordingCallback(microphoneCallback)
    }

    override fun eventActionByPackageName(eventPackageName: CharSequence) {
        if (!wasMicrophoneBeingUsed) {
            packageUsingMicrophone = eventPackageName.toString()
        } else {
            if (microphoneStartedUsageTime == null)
                microphoneStartedUsageTime = currentTimeMillis
        }
    }
}