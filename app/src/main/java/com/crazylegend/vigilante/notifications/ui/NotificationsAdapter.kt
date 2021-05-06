package com.crazylegend.vigilante.notifications.ui

import com.crazylegend.vigilante.databinding.ItemviewNotificationBinding
import com.crazylegend.vigilante.di.providers.prefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.notifications.db.NotificationsModel
import com.crazylegend.vigilante.paging.AbstractPagingAdapter
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/10/20 to long live and prosper !
 */
@FragmentScoped
class NotificationsAdapter @Inject constructor(private val prefsProvider: DefaultPreferencessProvider) :
    AbstractPagingAdapter<NotificationsModel,
            NotificationsViewHolder, ItemviewNotificationBinding>(
        ::NotificationsViewHolder,
        ItemviewNotificationBinding::inflate
    ) {
    override fun bindItems(
        item: NotificationsModel?,
        holder: NotificationsViewHolder,
        position: Int,
        itemCount: Int
    ) {
        item ?: return
        holder.bind(item, prefsProvider)
    }
}