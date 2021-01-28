package com.crazylegend.vigilante.notifications.ui

import android.app.Application
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.notifications.db.NotificationsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@HiltViewModel
class NotificationsVM @Inject constructor(
        private val repo: NotificationsRepo,
        application: Application) : AbstractAVM(application) {

    val notificationsAccess = provideDatabaseData { repo.getAllNotifications() }

}