package com.crazylegend.vigilante.gps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager.PROVIDERS_CHANGED_ACTION
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.vigilante.VigilanteService
import com.crazylegend.vigilante.utils.isGpsEnabled
import java.util.*


/**
 * Created by crazy on 10/17/20 to long live and prosper !
 */
class GPSReceiver : BroadcastReceiver() {

    private val currentPackageString: String? get() = VigilanteService.currentPackageString

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        context ?: return

        if (intent.action == PROVIDERS_CHANGED_ACTION)
            debug { "LOCATION ALRIGHT ${context.isGpsEnabled(currentPackageString)} ${Date(currentTimeMillis).toString("dd.MM.yyyy HH:mm:ss")}" }

        debug { "CURRENT PACKAGE USING LOCATION $currentPackageString" }
    }


}