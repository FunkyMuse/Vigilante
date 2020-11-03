package com.crazylegend.vigilante.microphone.ui

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.getAppIcon
import com.crazylegend.kotlinextensions.context.getAppName
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.context
import com.crazylegend.vigilante.databinding.ItemviewCameraAccessBinding
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.microphone.db.MicrophoneModel


/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class MicrophoneAccessViewHolder(private val binding: ItemviewCameraAccessBinding,
                                 private val prefsProvider: PrefsProvider) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MicrophoneModel) {
        binding.appName.setPrecomputedText(tryOrNull { context.getAppName(item.packageUsingCamera.toString()) })
        binding.appIcon.setImageDrawable(tryOrNull { context.getAppIcon(item.packageUsingCamera.toString()) })
        binding.date.setPrecomputedText(item.microphoneStartedUsageTime?.toString(prefsProvider.getDateFormat))
    }
}