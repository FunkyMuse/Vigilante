package com.crazylegend.vigilante.microphone.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crazylegend.kotlinextensions.singleton.ParameterizedSingleton
import com.crazylegend.vigilante.consts.MICROPHONE_DB_NAME
import com.crazylegend.vigilante.utils.DateTypeConverter
import com.crazylegend.vigilante.utils.instantiateDatabase

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@Database(entities = [MicrophoneModel::class], version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class MicrophoneDatabase : RoomDatabase() {

    abstract fun dao(): MicrophoneDAO

    companion object : ParameterizedSingleton<MicrophoneDatabase, Context>({ it.instantiateDatabase(MICROPHONE_DB_NAME) })
}