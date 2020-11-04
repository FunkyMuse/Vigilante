package com.crazylegend.vigilante.utils

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.getAppIcon
import com.crazylegend.kotlinextensions.context.getAppName
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.context
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.databinding.ItemviewLogBinding
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.microphone.db.MicrophoneModel


/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class LogViewHolder(private val binding: ItemviewLogBinding,
                    private val prefsProvider: PrefsProvider) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CameraModel) {
        binding.title.setPrecomputedText(tryOrNull { context.getAppName(item.packageUsingCamera.toString()) })
        binding.image.setImageDrawable(tryOrNull { context.getAppIcon(item.packageUsingCamera.toString()) })
        binding.content.setPrecomputedText(item.cameraStartedUsageTime?.toString(prefsProvider.getDateFormat))
    }

    fun bind(item: MicrophoneModel) {
        binding.title.setPrecomputedText(tryOrNull { context.getAppName(item.packageUsingCamera.toString()) })
        binding.image.setImageDrawable(tryOrNull { context.getAppIcon(item.packageUsingCamera.toString()) })
        binding.content.setPrecomputedText(item.microphoneStartedUsageTime?.toString(prefsProvider.getDateFormat))
    }
}