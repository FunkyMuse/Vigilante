package com.crazylegend.vigilante.utils

import androidx.room.TypeConverter
import java.util.*

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
