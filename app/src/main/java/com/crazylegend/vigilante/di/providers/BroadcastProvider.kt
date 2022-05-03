package com.crazylegend.vigilante.di.providers

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.common.ifTrue
import com.crazylegend.context.registerReceiverSafe
import com.crazylegend.context.unRegisterReceiverSafe
import com.crazylegend.vigilante.contracts.service.ServiceLifecycle
import com.crazylegend.vigilante.di.providers.prefs.logging.LoggingPrefs
import com.crazylegend.vigilante.headset.HeadsetReceiver
import com.crazylegend.vigilante.power.PowerReceiver
import com.crazylegend.vigilante.screen.ScreenReceiver
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */

@ServiceScoped
class BroadcastProvider @Inject constructor(
    private val screenReceiver: ScreenReceiver,
    private val headsetPlugReceiver: HeadsetReceiver,
    private val powerReceiver: PowerReceiver,
    private val service: Service,
    private val loggingPrefs: LoggingPrefs
) : ServiceLifecycle {

    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    override fun initVars() {}

    override fun registerCallbacks() {
        loggingPrefs.isHeadsetLoggingEnabled.ifTrue { registerHeadsetReceiver() }
        loggingPrefs.isLockScreenLoggingEnabled.ifTrue { registerScreenReceiver() }
        loggingPrefs.isPowerLoggingEnabled.ifTrue { registerPowerReceiver() }
    }

    override fun disposeResources() {
        service.unRegisterReceiverSafe(headsetPlugReceiver)
        service.unRegisterReceiverSafe(screenReceiver)
        service.unRegisterReceiverSafe(powerReceiver)
    }

    private fun registerPowerReceiver() {
        registerReceiver(Intent.ACTION_POWER_CONNECTED, powerReceiver) {
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
    }

    private fun registerHeadsetReceiver() {
        registerReceiver(Intent.ACTION_HEADSET_PLUG, headsetPlugReceiver)
    }

    private fun registerScreenReceiver() {
        registerReceiver(Intent.ACTION_SCREEN_ON, screenReceiver) {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
    }

    private inline fun registerReceiver(
        initialAction: String,
        broadcastReceiver: BroadcastReceiver,
        filterConfig: IntentFilter.() -> Unit = {}
    ) {
        val filter = IntentFilter(initialAction)
        filter.filterConfig()
        service.registerReceiverSafe(broadcastReceiver, filter)
    }
}