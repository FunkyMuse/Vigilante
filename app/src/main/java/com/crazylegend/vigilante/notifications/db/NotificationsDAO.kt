package com.crazylegend.vigilante.notifications.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@Dao
interface NotificationsDAO {

    @Insert
    fun insertNotificationModel(notificationsModel: NotificationsModel)

    @Query("select * from notifications")
    fun getAllNotifications(): PagingSource<Int, NotificationsModel>

    @Query("select * from notifications where id =:notificationID limit 1")
    suspend fun getNotificationByID(notificationID: Int): NotificationsModel?
}