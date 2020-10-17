package com.crazylegend.vigilante.gps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.crazylegend.kotlinextensions.log.debug


/**
 * Created by crazy on 10/17/20 to long live and prosper !
 */
class GPSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        context ?: return
        debug { "LOCATION ALRIGHT ${isGpsEnabled(context)}" }
    }

    private fun isGpsEnabled(context: Context): GPSModel {
        val mode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
        return if (mode != Settings.Secure.LOCATION_MODE_OFF) {
            val locationMode = when (mode) {
                Settings.Secure.LOCATION_MODE_HIGH_ACCURACY -> {
                    "High accuracy. Uses GPS, Wi-Fi, and mobile networks to determine location";
                }
                Settings.Secure.LOCATION_MODE_SENSORS_ONLY -> {
                    "Device only. Uses GPS to determine location";
                }
                Settings.Secure.LOCATION_MODE_BATTERY_SAVING -> {
                    "Battery saving. Uses Wi-Fi and mobile networks to determine location";
                }
                else -> null
            }
            GPSModel(true, locationMode)
        } else {
            GPSModel(false)
        }
    }

}