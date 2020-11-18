package com.crazylegend.vigilante.database


import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crazylegend.kotlinextensions.singleton.ParameterizedSingleton
import com.crazylegend.vigilante.headset.database.HeadsetDAO
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.notifications.db.NotificationsDAO
import com.crazylegend.vigilante.notifications.db.NotificationsModel
import com.crazylegend.vigilante.permissions.db.PermissionRequestModel
import com.crazylegend.vigilante.permissions.db.PermissionRequestsDAO
import com.crazylegend.vigilante.power.db.PowerDAO
import com.crazylegend.vigilante.power.db.PowerModel
import com.crazylegend.vigilante.screen.db.ScreenDAO
import com.crazylegend.vigilante.screen.db.ScreenModel
import com.crazylegend.vigilante.utils.DateTypeConverter
import com.crazylegend.vigilante.utils.VIGILANTE_DB_NAME
import com.crazylegend.vigilante.utils.instantiateDatabase

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@Database(entities = [
    HeadsetModel::class,
    NotificationsModel::class,
    ScreenModel::class,
    PermissionRequestModel::class,
    PowerModel::class
], version = 2, exportSchema = true)
@TypeConverters(DateTypeConverter::class)
abstract class VigilanteDatabase : RoomDatabase() {

    abstract fun headsetDao(): HeadsetDAO
    abstract fun notificationsDAO(): NotificationsDAO
    abstract fun screenDAO(): ScreenDAO
    abstract fun permissionRequestsDAO(): PermissionRequestsDAO
    abstract fun powerDAO(): PowerDAO

    companion object : ParameterizedSingleton<VigilanteDatabase, Context>({ it.instantiateDatabase(VIGILANTE_DB_NAME) })
}