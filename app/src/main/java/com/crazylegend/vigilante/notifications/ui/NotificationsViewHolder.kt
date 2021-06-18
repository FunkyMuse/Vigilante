package com.crazylegend.vigilante.notifications.ui

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.common.tryOrNull
import com.crazylegend.context.getAppIcon
import com.crazylegend.context.getAppName
import com.crazylegend.datetime.toString
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.context
import com.crazylegend.vigilante.databinding.ItemviewNotificationBinding
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.notifications.db.NotificationsModel

/**
 * Created by funkymuse on 5/5/21 to long live and prosper !
 */
class NotificationsViewHolder(private val binding: ItemviewNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationsModel, prefsProvider: DefaultPreferencessProvider) {
        binding.title.setPrecomputedText(tryOrNull { context.getAppName(item.sentByPackage.toString()) })
        binding.image.setImageDrawable(tryOrNull { context.getAppIcon(item.sentByPackage.toString()) })
        binding.date.setPrecomputedText(item.showTime.toString(prefsProvider.getDateFormat))
        binding.content.isVisible = !item.title.isNullOrBlank()
        binding.content.setPrecomputedText(item.title)
        val content = item.text ?: item.bigText
        binding.text.setPrecomputedText(content)
        binding.text.isVisible = !content.isNullOrBlank()
    }
}