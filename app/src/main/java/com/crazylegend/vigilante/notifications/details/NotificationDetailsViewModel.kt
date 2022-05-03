package com.crazylegend.vigilante.notifications.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.database.coroutines.dbCallStateFlow
import com.crazylegend.vigilante.notifications.db.NotificationsDAO
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class NotificationDetailsViewModel @AssistedInject constructor(
    private val notificationsRepo: NotificationsDAO,
    @Assisted private val notificationID: Int) : ViewModel() {

    @AssistedFactory
    interface NotificationDetailsVMFactory {
        fun create(notificationID: Int): NotificationDetailsViewModel
    }

    val notification = viewModelScope.dbCallStateFlow {
        notificationsRepo.getNotificationByID(notificationID)
    }
}