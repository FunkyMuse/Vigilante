package com.crazylegend.vigilante.power

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.common.batteryStatusIntent
import com.crazylegend.common.getBatteryInfo
import com.crazylegend.vigilante.di.modules.coroutines.appScope.ApplicationScope
import com.crazylegend.vigilante.power.db.PowerDAO
import com.crazylegend.vigilante.power.db.PowerModel
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
@ServiceScoped
class PowerReceiver @Inject constructor(private val powerRepository: PowerDAO,
                                        @ApplicationScope private val appScope: CoroutineScope) : BroadcastReceiver() {

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
        appScope.launch {
            withContext(NonCancellable) {
                powerRepository.insertPowerAction(powerModel)
            }
        }
    }
}