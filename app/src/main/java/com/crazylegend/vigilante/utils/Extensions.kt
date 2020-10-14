@file:Suppress("DEPRECATION")

package com.crazylegend.vigilante.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.crazylegend.kotlinextensions.activity.newIntent
import com.crazylegend.kotlinextensions.context.activityManager
import com.crazylegend.vigilante.VigilanteService

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

fun Context.startVigilante() {
    val serviceIntent = Intent(this, VigilanteService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(serviceIntent)
    } else {
        startService(serviceIntent)
    }
}

fun Context.stopVigilante(): Boolean {
    val intent = newIntent<VigilanteService>(this)
    return stopService(intent)
}

fun Context.isVigilanteRunning(): Boolean {
    for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
        if (VigilanteService::class.java.name == service.service.className) {
            return true
        }
    }
    return false
}