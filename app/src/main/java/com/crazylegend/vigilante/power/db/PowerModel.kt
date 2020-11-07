package com.crazylegend.vigilante.power.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.vigilante.R
import java.util.*

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */

@Entity(tableName = "powerActions")
data class PowerModel(
        /**
         * AC Charge = 1
         * USB Charge = 2
         * Wireless charge = 3
         * Invalid type  = -1
         */
        @ColumnInfo(name = "chargingType")
        val chargingType: Int,

        @ColumnInfo(name = "dateOfEvent")
        val date: Date = Date(currentTimeMillis),

        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,

        @ColumnInfo(name = "batteryPercentage")
        val batteryPercentage: Int,

        @ColumnInfo(name = "isCharging")
        val isCharging: Boolean = false,

        ) {

    val chargingTitle get() = if (isCharging) R.string.power_connected else R.string.power_disconnected
    val chargingDrawable get() = if (isCharging) R.drawable.ic_power_on else R.drawable.ic_power_off


    val chargingTypeTitle
        get() = when (chargingType) {
            1 -> R.string.ac_charging
            2 -> R.string.usb_charging
            3 -> R.string.wireless_charging
            else -> null
        }

    val chargingTypeDrawable
        get() = when (chargingType) {
            1 -> R.drawable.ic_charging_ac
            2 -> R.drawable.ic_charging_usb
            3 -> R.drawable.ic_charging_wireless
            else -> null
        }
}