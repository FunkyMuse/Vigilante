package com.crazylegend.vigilante.camera.ui

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.getAppIcon
import com.crazylegend.kotlinextensions.context.getAppName
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.context
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.databinding.ItemviewCameraAccessBinding
import com.crazylegend.vigilante.di.providers.PrefsProvider


/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class CameraAccessViewHolder(private val binding: ItemviewCameraAccessBinding,
                             private val prefsProvider: PrefsProvider) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CameraModel) {
        debug { "MODEL $item" }
        binding.appName.setPrecomputedText(tryOrNull { context.getAppName(item.packageUsingCamera.toString()) })
        binding.appIcon.setImageDrawable(tryOrNull { context.getAppIcon(item.packageUsingCamera.toString()) })
        binding.date.setPrecomputedText(item.cameraStartedUsageTime?.toString(prefsProvider.getDateFormat))
    }
}