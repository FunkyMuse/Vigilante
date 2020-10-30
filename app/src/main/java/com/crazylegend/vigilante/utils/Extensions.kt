@file:Suppress("DEPRECATION")

package com.crazylegend.vigilante.utils

import android.content.Context
import com.crazylegend.kotlinextensions.activity.newIntent
import com.crazylegend.kotlinextensions.services.isServiceRunning
import com.crazylegend.kotlinextensions.services.startForegroundService
import com.crazylegend.vigilante.VigilanteService

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