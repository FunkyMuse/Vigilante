package com.crazylegend.vigilante.notifications.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crazylegend.kotlinextensions.randomUUIDstring
import java.util.*

/**
 * Created by crazy on 10/20/20 to long live and prosper !
 */


@Entity(tableName = "notifications")
data class NotificationsModel(
        @ColumnInfo(name = "title")
        val title: String?,

        @ColumnInfo(name = "bigText")
        val bigText: String?,

        @ColumnInfo(name = "text")
        val text: String?,

        @ColumnInfo(name = "visibility")
        val visibility: Int?,

        @ColumnInfo(name = "category")
        val category: String?,

        @ColumnInfo(name = "color")
        val color: Int?,

        @ColumnInfo(name = "flags")
        val flags: Int?,

        @ColumnInfo(name = "group")
        val group: String?,

        @ColumnInfo(name = "channelId")
        val channelId: String?,

        @ColumnInfo(name = "sentByPackage")
        val sentByPackage: String?,

        @ColumnInfo(name = "showTime")
        val showTime: Date,

        @ColumnInfo(name = "id")
        @PrimaryKey
        val id: String = randomUUIDstring
)