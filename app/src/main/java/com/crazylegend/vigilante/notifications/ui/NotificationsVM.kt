package com.crazylegend.vigilante.notifications.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.notifications.db.NotificationsRepo

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class NotificationsVM @ViewModelInject constructor(
        private val repo: NotificationsRepo,
        application: Application) : AbstractAVM(application) {

    val notificationsAccess = provideDatabaseData { repo.getAllNotifications() }

}