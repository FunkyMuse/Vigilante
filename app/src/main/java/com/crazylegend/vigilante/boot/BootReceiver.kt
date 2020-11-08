package com.crazylegend.vigilante.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.kotlinextensions.accessibility.hasAccessibilityPermission
import com.crazylegend.vigilante.service.VigilanteService
import com.crazylegend.vigilante.utils.isVigilanteRunning
import com.crazylegend.vigilante.utils.startVigilante

/**
 * Created by crazy on 10/19/20 to long live and prosper !
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        context ?: return

        val action = intent.action ?: return

        if (action == Intent.ACTION_BOOT_COMPLETED || action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            if (context.hasAccessibilityPermission<VigilanteService>() && !context.isVigilanteRunning()) {
                context.startVigilante()
            }
        }
    }
}