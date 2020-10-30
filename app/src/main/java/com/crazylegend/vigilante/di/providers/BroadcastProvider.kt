package com.crazylegend.vigilante.di.providers

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.BatteryManager
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.kotlinextensions.batteryStatusIntent
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.gps.GPSReceiver
import com.crazylegend.vigilante.headset.HeadsetReceiver
import com.crazylegend.vigilante.screen.ScreenReceiver
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */

@ServiceScoped
class BroadcastProvider @Inject constructor(private val service: Service,
                                            @ServiceContext private val context: Context) : ServiceManagersCoroutines {

    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    private lateinit var gpsReceiver: GPSReceiver
    private lateinit var screenReceiver: ScreenReceiver
    private lateinit var headsetPlugReceiver: HeadsetReceiver

    private val receivers
        get() = listOf(
                gpsReceiver, screenReceiver, headsetPlugReceiver
        )

    override fun initVars() {
        gpsReceiver = GPSReceiver()
        screenReceiver = ScreenReceiver()
        headsetPlugReceiver = HeadsetReceiver()
    }

    override fun registerCallbacks() {
        registerGPSReceiver()
        registerHeadsetReceiver()
        registerScreenReceiver()
        registerPowerReceiver()
    }

    override fun disposeResources() {
        receivers.asSequence().forEach { service.unregisterReceiver(it) }
    }

    override fun eventActionByPackageName(eventPackageName: CharSequence) {}

    private fun registerGPSReceiver() {
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        service.registerReceiver(gpsReceiver, filter)
    }

    private fun registerPowerReceiver() {
        val batteryIntent = context.batteryStatusIntent ?: return
        val status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        val chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
        val wirelessCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS

        val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        val batteryPct = level / scale.toFloat()
        debug { "IS BATTERY CHARGING $isCharging usbCharge=${usbCharge} acCharge=$acCharge wirelessCharge=$wirelessCharge percentage=$batteryPct" }
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