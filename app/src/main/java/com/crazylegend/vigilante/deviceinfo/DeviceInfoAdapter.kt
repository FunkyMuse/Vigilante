package com.crazylegend.vigilante.deviceinfo

import android.content.Context
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.AbstractViewBindingHolderAdapter
import com.crazylegend.vigilante.databinding.ItemviewDeviceInfoBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by Hristijan, date 10/13/21
 */
@FragmentScoped
class DeviceInfoAdapter @Inject constructor() : AbstractViewBindingHolderAdapter<DeviceInfoModel, ItemviewDeviceInfoBinding>(ItemviewDeviceInfoBinding::inflate) {

    override fun bindItems(item: DeviceInfoModel, position: Int, itemCount: Int, binding: ItemviewDeviceInfoBinding, context: Context) {
        binding.title.setPrecomputedText(context.getString(item.title))
        binding.content.setPrecomputedText(context.getString(item.content))
    }
}