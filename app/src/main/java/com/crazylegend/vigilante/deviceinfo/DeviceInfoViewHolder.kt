package com.crazylegend.vigilante.deviceinfo

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.getString
import com.crazylegend.vigilante.databinding.ItemviewDeviceInfoBinding

/**
 * Created by crazy on 11/9/20 to long live and prosper !
 */
class DeviceInfoViewHolder(private val binding: ItemviewDeviceInfoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DeviceInfoModel) {
        binding.title.setPrecomputedText(getString(item.title))
        binding.content.setPrecomputedText(getString(item.content))
    }
}