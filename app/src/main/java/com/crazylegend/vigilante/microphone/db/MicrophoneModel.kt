package com.crazylegend.vigilante.microphone.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */

@Entity(tableName = "microphoneAccesses")
data class MicrophoneModel(@ColumnInfo(name = "microphoneStartedUsageTime")
                           val microphoneStartedUsageTime: Date? = null,

                           @ColumnInfo(name = "packageUsingCamera")
                           val packageUsingCamera: String? = null,

                           @ColumnInfo(name = "microphoneStoppedBeingUsedAt")
                           val microphoneStoppedBeingUsedAt: Date? = null,

                           @ColumnInfo(name = "id")
                           @PrimaryKey(autoGenerate = true)
                           val id: Int = 0)