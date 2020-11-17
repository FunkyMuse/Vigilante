package com.crazylegend.vigilante.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Created by crazy on 11/17/20 to long live and prosper !
 */
class CameraAndMicRemovalMigration : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("drop table if exists cameraAccesses")
        database.execSQL("drop table if exists microphoneAccesses")
    }
}