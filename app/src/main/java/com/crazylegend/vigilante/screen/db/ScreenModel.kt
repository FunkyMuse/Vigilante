package com.crazylegend.vigilante.screen.db

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crazylegend.common.currentTimeMillis
import com.crazylegend.common.randomUUIDstring
import com.crazylegend.vigilante.R
import java.util.*

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */

@Entity(tableName = "screenActions")
data class ScreenModel(
        @ColumnInfo(name = "screenActionTime")
        val screenActionTime: Date = Date(currentTimeMillis),

        @ColumnInfo(name = "wasScreenLocked")
        val wasScreenLocked: Boolean = false,

        @ColumnInfo(name = "id")
        @PrimaryKey
        val id: String = randomUUIDstring
) {
    val screenRes get() = if (wasScreenLocked) R.drawable.lock else R.drawable.ic_unlocked
    fun screenTitle(context: Context) = context.getString(if (wasScreenLocked) R.string.screen_locked else R.string.screen_unlocked)
}