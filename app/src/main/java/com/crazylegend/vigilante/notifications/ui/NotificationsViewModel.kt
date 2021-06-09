package com.crazylegend.vigilante.notifications.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.vigilante.notifications.db.NotificationsRepo
import com.crazylegend.vigilante.paging.PagingProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repo: NotificationsRepo,
    pagingProvider: PagingProvider
) : ViewModel() {

    val notificationsAccess =
        pagingProvider.provideDatabaseData(viewModelScope) { repo.getAllNotifications() }

}