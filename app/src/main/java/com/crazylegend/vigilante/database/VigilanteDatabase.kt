package com.crazylegend.vigilante.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crazylegend.kotlinextensions.singleton.ParameterizedSingleton
import com.crazylegend.vigilante.camera.db.CameraDao
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.headset.database.HeadsetDAO
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.microphone.db.MicrophoneDAO
import com.crazylegend.vigilante.microphone.db.MicrophoneModel
import com.crazylegend.vigilante.notifications.db.NotificationsDAO
import com.crazylegend.vigilante.notifications.db.NotificationsModel
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
    CameraModel::class,
    NotificationsModel::class,
    MicrophoneModel::class,
    ScreenModel::class,
], version = 1, exportSchema = true)
@TypeConverters(DateTypeConverter::class)
abstract class VigilanteDatabase : RoomDatabase() {

    abstract fun headsetDao(): HeadsetDAO
    abstract fun cameraDao(): CameraDao
    abstract fun notificationsDAO(): NotificationsDAO
    abstract fun microphoneDAO(): MicrophoneDAO
    abstract fun screenDAO(): ScreenDAO

    companion object : ParameterizedSingleton<VigilanteDatabase, Context>({ it.instantiateDatabase(VIGILANTE_DB_NAME) })
}