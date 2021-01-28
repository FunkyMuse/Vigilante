package com.crazylegend.vigilante.notifications.details

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.crazylegend.database.coroutines.dbCallStateFlow
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.notifications.db.NotificationsRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class NotificationDetailsVM @AssistedInject constructor(
        private val notificationsRepo: NotificationsRepo,
        application: Application,
        @Assisted private val notificationID: Int) : AbstractAVM(application) {

    @AssistedFactory
    interface NotificationDetailsVMFactory {
        fun create(notificationID: Int): NotificationDetailsVM
    }

    val notification = viewModelScope.dbCallStateFlow {
        notificationsRepo.getNotificationByID(notificationID)
    }
}