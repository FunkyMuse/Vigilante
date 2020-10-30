@file:Suppress("DEPRECATION")

package com.crazylegend.vigilante.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.crazylegend.kotlinextensions.activity.newIntent
import com.crazylegend.kotlinextensions.services.isServiceRunning
import com.crazylegend.kotlinextensions.services.startForegroundService
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.VigilanteService
import com.crazylegend.vigilante.gps.GPSModel

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

fun Context.startVigilante() {
    startForegroundService<VigilanteService>()
}

fun Context.stopVigilante(): Boolean {
    val intent = newIntent<VigilanteService>(this)
    return stopService(intent)
}

fun Context.isVigilanteRunning() = isServiceRunning<VigilanteService>()

fun Context.isGpsEnabled(currentPackageString: String? = null): GPSModel {
    val mode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
    return if (mode != Settings.Secure.LOCATION_MODE_OFF) {
        val locationMode = when (mode) {
            Settings.Secure.LOCATION_MODE_HIGH_ACCURACY -> {
                getString(R.string.high_accuracy_location);
            }
            Settings.Secure.LOCATION_MODE_SENSORS_ONLY -> {
                getString(R.string.device_only_location);
            }
            Settings.Secure.LOCATION_MODE_BATTERY_SAVING -> {
                getString(R.string.battery_saving_location);
            }
            else -> null
        }
        GPSModel(true, locationMode, currentPackageString)
    } else {
        GPSModel(false, currentPackage = currentPackageString)
    }
}


inline fun Intent.onAction(intentAction: String, function: () -> Unit) {
    if (action == intentAction) function()
}