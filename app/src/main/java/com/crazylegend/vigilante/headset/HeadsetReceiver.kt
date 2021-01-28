package com.crazylegend.vigilante.headset

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.kotlinextensions.batteryStatusIntent
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.kotlinextensions.getBatteryInfo
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.headset.database.HeadsetRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */
@AndroidEntryPoint
class HeadsetReceiver : BroadcastReceiver() {

    @Inject
    lateinit var headsetRepository: HeadsetRepository

    private val lastState = AtomicInteger(-1)

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        context ?: return
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {

            when (val connectionType = intent.getIntExtra("state", -1)) {
                0 -> {
                    lastState.set(0)
                    saveToDatabase(connectionType, context)
                }
                1 -> {
                    if (lastState.getAndIncrement() == 1) {
                        saveToDatabase(connectionType, context)
                    }
                }
            }
        }
    }

    private fun saveToDatabase(connectionType: Int, context: Context) {
        val batteryIntent = context.batteryStatusIntent ?: return
        val batteryStatusModel = getBatteryInfo(batteryIntent)
        val headsetModel = HeadsetModel(Date(currentTimeMillis), connectionType,
                batteryStatusModel.batteryPercentage, batteryStatusModel.chargingType)
        GlobalScope.launch(NonCancellable) {
            headsetRepository.addHeadsetRecord(headsetModel)
        }
    }
}