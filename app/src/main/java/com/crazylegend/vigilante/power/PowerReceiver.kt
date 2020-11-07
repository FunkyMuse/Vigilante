package com.crazylegend.vigilante.power

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.kotlinextensions.batteryStatusIntent
import com.crazylegend.kotlinextensions.getBatteryInfo
import com.crazylegend.vigilante.power.db.PowerModel
import com.crazylegend.vigilante.power.db.PowerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
@AndroidEntryPoint
class PowerReceiver : BroadcastReceiver() {

    @Inject
    lateinit var powerRepository: PowerRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val isCharging = when (intent.action) {
            Intent.ACTION_POWER_DISCONNECTED -> false
            Intent.ACTION_POWER_CONNECTED -> true
            else -> null
        }
        isCharging?.apply {
            saveAction(this, context)
        }
    }

    private fun saveAction(isCharging: Boolean, context: Context) {
        val batteryIntent = context.batteryStatusIntent ?: return
        val batteryStatusModel = getBatteryInfo(batteryIntent)
        val powerModel = PowerModel(
                chargingType = batteryStatusModel.chargingType,
                batteryPercentage = batteryStatusModel.batteryPercentage,
                isCharging = isCharging)
        GlobalScope.launch(ioDispatcher) {
            withContext(NonCancellable) {
                powerRepository.insertPowerAction(powerModel)
            }
        }
    }
}