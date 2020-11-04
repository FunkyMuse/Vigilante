package com.crazylegend.vigilante.notifications.db

import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class NotificationsRepo @Inject constructor(private val dao: NotificationsDAO) {

    fun getAllNotifications() = dao.getAllNotifications()
    fun insertNotification(notificationsModel: NotificationsModel) = dao.insertNotificationModel(notificationsModel)
}