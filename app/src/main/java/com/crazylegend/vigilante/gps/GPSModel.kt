package com.crazylegend.vigilante.gps

import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.kotlinextensions.dateAndTime.toString
import java.util.*

/**
 * Created by crazy on 10/17/20 to long live and prosper !
 */
data class GPSModel(val isEnabled: Boolean,
                    val locationMode: String? = null,

                    val currentPackage: String? = null,

                    /**
                     * Should be primary key for future references
                     */
                    val time: Long = currentTimeMillis) {

    fun userReadableTime(format: String) = Date(currentTimeMillis).toString(format)
}