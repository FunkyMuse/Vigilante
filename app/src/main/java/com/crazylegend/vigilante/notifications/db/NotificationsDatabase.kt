package com.crazylegend.vigilante.notifications.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crazylegend.kotlinextensions.singleton.ParameterizedSingleton
import com.crazylegend.vigilante.consts.NOTIFICATIONS_DB_NAME
import com.crazylegend.vigilante.utils.DateTypeConverter
import com.crazylegend.vigilante.utils.instantiateDatabase

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */

@Database(entities = [NotificationsModel::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class NotificationsDatabase : RoomDatabase() {
    abstract fun dao(): NotificationsDAO

    companion object : ParameterizedSingleton<NotificationsDatabase, Context>({
        it.instantiateDatabase(NOTIFICATIONS_DB_NAME)
    })
}