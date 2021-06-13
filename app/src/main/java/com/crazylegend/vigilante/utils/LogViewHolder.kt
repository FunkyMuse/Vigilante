package com.crazylegend.vigilante.utils

import android.app.usage.UsageStats
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.getAppIcon
import com.crazylegend.kotlinextensions.context.getAppName
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.context
import com.crazylegend.recyclerview.getString
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.databinding.ItemviewLogBinding
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.permissions.db.PermissionRequestModel
import com.crazylegend.vigilante.power.db.PowerModel
import com.crazylegend.vigilante.screen.db.ScreenModel
import java.util.*


/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class LogViewHolder(private val binding: ItemviewLogBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: UsageStats, prefsProvider: DefaultPreferencessProvider) {
        binding.title.setPrecomputedText(tryOrNull { context.getAppName(item.packageName.toString()) })
        binding.image.setImageDrawable(tryOrNull { context.getAppIcon(item.packageName.toString()) })
        binding.content.setPrecomputedText(Date(item.lastTimeUsed).toString(prefsProvider.getDateFormat))
    }

    fun bind(screenModel: ScreenModel, prefsProvider: DefaultPreferencessProvider) {
        binding.title.setPrecomputedText(screenModel.screenTitle(context))
        binding.content.setPrecomputedText(screenModel.screenActionTime.toString(prefsProvider.getDateFormat))
        binding.image.setImageResource(screenModel.screenRes)
    }

    fun bind(item: HeadsetModel, prefsProvider: DefaultPreferencessProvider) {
        binding.title.setPrecomputedText(item.connectionTypeTitle(context))
        binding.image.setImageResource(R.drawable.headphones)
        binding.content.setPrecomputedText(item.headsetActionTime?.toString(prefsProvider.getDateFormat))
    }

    fun bind(item: PermissionRequestModel, prefsProvider: DefaultPreferencessProvider) {
        binding.title.setPrecomputedText(tryOrNull { context.getAppName(item.packageRequestingThePermission.toString()) })
        binding.image.setImageDrawable(tryOrNull { context.getAppIcon(item.packageRequestingThePermission.toString()) })
        binding.content.setPrecomputedText(item.date.toString(prefsProvider.getDateFormat))
    }

    fun bind(item: PowerModel, prefsProvider: DefaultPreferencessProvider) {
        binding.title.setPrecomputedText(getString(item.chargingTitle))
        binding.image.setImageResource(item.chargingDrawable)
        binding.content.setPrecomputedText(item.date.toString(prefsProvider.getDateFormat))
    }

}