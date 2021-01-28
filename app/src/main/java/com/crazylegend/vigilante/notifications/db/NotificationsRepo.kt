package com.crazylegend.vigilante.notifications.db

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@Singleton
class NotificationsRepo @Inject constructor(private val dao: NotificationsDAO) : NotificationsDAO by dao