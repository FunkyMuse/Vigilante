package com.crazylegend.vigilante.screen.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crazylegend.kotlinextensions.singleton.ParameterizedSingleton
import com.crazylegend.vigilante.consts.SCREEN_DB_NAME
import com.crazylegend.vigilante.utils.DateTypeConverter
import com.crazylegend.vigilante.utils.instantiateDatabase

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */

@Database(entities = [ScreenModel::class], exportSchema = false, version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class ScreenDatabase : RoomDatabase() {
    abstract fun dao(): ScreenDao

    companion object : ParameterizedSingleton<ScreenDatabase, Context>({ it.instantiateDatabase(SCREEN_DB_NAME) })


}