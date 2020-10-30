package com.crazylegend.vigilante.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.kotlinextensions.log.debug

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */
class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return

        if (intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
            debug { "SCREEN TURNED OFF" }
        } else if (intent.action.equals(Intent.ACTION_SCREEN_ON)) {
            debug { "SCREEN TURNED ON" }
        }

    }
}