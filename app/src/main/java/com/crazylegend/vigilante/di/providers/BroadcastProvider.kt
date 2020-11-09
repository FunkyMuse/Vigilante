package com.crazylegend.vigilante.di.providers

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.headset.HeadsetReceiver
import com.crazylegend.vigilante.power.PowerReceiver
import com.crazylegend.vigilante.screen.ScreenReceiver
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */

@ServiceScoped
class BroadcastProvider @Inject constructor(private val service: Service) : ServiceManagersCoroutines {

    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    private lateinit var screenReceiver: ScreenReceiver
    private lateinit var headsetPlugReceiver: HeadsetReceiver
    private lateinit var powerReceiver: PowerReceiver

    private val receivers
        get() = listOf(
                screenReceiver, headsetPlugReceiver, powerReceiver
        )

    override fun initVars() {
        screenReceiver = ScreenReceiver()
        headsetPlugReceiver = HeadsetReceiver()
        powerReceiver = PowerReceiver()
    }

    override fun registerCallbacks() {
        registerHeadsetReceiver()
        registerScreenReceiver()
        registerPowerReceiver()
        registerPowerReceiver()
    }

    override fun disposeResources() {
        receivers.asSequence().forEach { service.unregisterReceiver(it) }
    }

    override fun eventActionByPackageName(eventPackageName: CharSequence) {}

    private fun registerPowerReceiver() {
        val filter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        service.registerReceiver(powerReceiver, filter)
    }

    private fun registerHeadsetReceiver() {
        val filter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        service.registerReceiver(headsetPlugReceiver, filter)
    }

    private fun registerScreenReceiver() {
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        service.registerReceiver(screenReceiver, filter)
    }
}