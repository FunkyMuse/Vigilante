package com.crazylegend.vigilante.headset

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.kotlinextensions.context.longToast

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */
class HeadsetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            val isConnected = intent.getIntExtra("state", 0) != 0 // Can also be 2 if headset is attached w/o mic.
            context?.longToast("IS HEADSET CONNECTED $isConnected")
        }
    }
}