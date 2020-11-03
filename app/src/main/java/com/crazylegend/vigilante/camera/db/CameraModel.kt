package com.crazylegend.vigilante.camera.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */

@Entity(tableName = "cameraAccesses")
data class CameraModel(

        @ColumnInfo(name = "cameraStartedUsageTime")
        val cameraStartedUsageTime: Date? = null,

        @ColumnInfo(name = "packageUsingCamera")
        val packageUsingCamera: String? = null,

        @ColumnInfo(name = "cameraId")
        val cameraId: String? = null,

        @ColumnInfo(name = "cameraStoppedBeingUsedAt")
        val cameraStoppedBeingUsedAt: Date? = null,

        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0)