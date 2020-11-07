package com.crazylegend.vigilante.notifications.details

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.crazylegend.database.coroutines.makeDBCallLiveData
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.notifications.db.NotificationsRepo

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class NotificationDetailsVM @ViewModelInject constructor(
        private val notificationsRepo: NotificationsRepo,
        application: Application,
        @Assisted private val savedStateHandle: SavedStateHandle) : AbstractAVM(application) {

    private val notificationID get() = savedStateHandle.get<Int>("notificationID") ?: -1

    val notification = makeDBCallLiveData {
        notificationsRepo.getNotificationForID(notificationID)
    }
}