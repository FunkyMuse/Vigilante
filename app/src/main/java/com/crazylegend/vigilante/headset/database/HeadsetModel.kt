package com.crazylegend.vigilante.headset.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crazylegend.vigilante.R
import java.util.*

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */

@Entity(tableName = "headsetAccesses")
data class HeadsetModel(

        @ColumnInfo(name = "headsetActionTime")
        val headsetActionTime: Date? = null,

        /**
         * 0 - disconnected
         * 1 - connected
         * 2 - connected with mic
         */
        @ColumnInfo(name = "headsetUsageType")
        val headsetUsageType: Int,

        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0) {

    fun connectionTypeTitle(context: Context): String {
        val stringRes = when (headsetUsageType) {
            0 -> R.string.disconnected
            1 -> R.string.connected
            2 -> R.string.connected_with_mic
            else -> R.string.not_available
        }
        return context.getString(R.string.headset_connection_type_title, context.getString(stringRes))
    }
}