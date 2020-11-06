package com.crazylegend.vigilante.permissions.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crazylegend.kotlinextensions.currentTimeMillis
import java.util.*

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */

@Entity(tableName = "permissionRequests")
data class PermissionRequestModel(

        @ColumnInfo(name = "permissionMessage")
        val permissionMessage: String?,

        @ColumnInfo(name = "packageRequestingThePermission")
        val packageRequestingThePermission: String?,

        @ColumnInfo(name = "dateOfRequest")
        val date: Date = Date(currentTimeMillis),

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0,

        )