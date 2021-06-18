package com.crazylegend.vigilante.headset

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.common.batteryStatusIntent
import com.crazylegend.common.currentTimeMillis
import com.crazylegend.common.getBatteryInfo
import com.crazylegend.vigilante.di.modules.coroutines.appScope.ApplicationScope
import com.crazylegend.vigilante.headset.database.HeadsetDAO
import com.crazylegend.vigilante.headset.database.HeadsetModel
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */
@ServiceScoped
class HeadsetReceiver @Inject constructor(private val headsetRepository: HeadsetDAO,
                                          @ApplicationScope private val appScope: CoroutineScope) : BroadcastReceiver() {

    private companion object {
        private const val STATE_EXTRA_TAG = "state"
    }

    private val lastState = AtomicInteger(-1)

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        context ?: return
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {

            when (val connectionType = intent.getIntExtra(STATE_EXTRA_TAG, -1)) {
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
        appScope.launch {
            withContext(NonCancellable) {
                headsetRepository.addHeadsetRecord(headsetModel)
            }
        }
    }
}