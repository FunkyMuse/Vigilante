package com.crazylegend.vigilante.camera.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crazylegend.kotlinextensions.singleton.ParameterizedSingleton
import com.crazylegend.vigilante.consts.CAMERA_DB_NAME
import com.crazylegend.vigilante.utils.DateTypeConverter
import com.crazylegend.vigilante.utils.instantiateDatabase


/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */

@Database(entities = [CameraModel::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class CameraDatabase : RoomDatabase() {

    abstract fun dao(): CameraDao

    companion object : ParameterizedSingleton<CameraDatabase, Context>({ it.instantiateDatabase(CAMERA_DB_NAME) })
}