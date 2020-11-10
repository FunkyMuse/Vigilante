package com.crazylegend.vigilante.di.providers

import com.crazylegend.recyclerview.generateRecycler
import com.crazylegend.vigilante.crashes.CrashViewHolder
import com.crazylegend.vigilante.databinding.ItemviewCrashBinding
import com.crazylegend.vigilante.databinding.ItemviewDeviceInfoBinding
import com.crazylegend.vigilante.databinding.ItemviewFilterBinding
import com.crazylegend.vigilante.databinding.ItemviewSectionBinding
import com.crazylegend.vigilante.deviceinfo.DeviceInfoModel
import com.crazylegend.vigilante.deviceinfo.DeviceInfoViewHolder
import com.crazylegend.vigilante.filter.FilterModel
import com.crazylegend.vigilante.filter.ListFilterViewHolder
import com.crazylegend.vigilante.home.section.SectionItem
import com.crazylegend.vigilante.home.section.SectionViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@FragmentScoped
class AdapterProvider @Inject constructor() {

    val listFilterAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generateRecycler<FilterModel, ListFilterViewHolder, ItemviewFilterBinding>(::ListFilterViewHolder, ItemviewFilterBinding::inflate) { item, holder, position, itemCount ->
            holder.bind(item, position, itemCount)
        }
    }

    val sectionAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generateRecycler<SectionItem, SectionViewHolder, ItemviewSectionBinding>(::SectionViewHolder, ItemviewSectionBinding::inflate) { item, holder, position, itemCount ->
            holder.bind(item, position, itemCount)
        }
    }

    val crashesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generateRecycler<String, CrashViewHolder, ItemviewCrashBinding>(::CrashViewHolder, ItemviewCrashBinding::inflate) { _, holder, position, _ ->
            holder.bind(position)
        }
    }

    val deviceInfoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generateRecycler<DeviceInfoModel, DeviceInfoViewHolder, ItemviewDeviceInfoBinding>(::DeviceInfoViewHolder, ItemviewDeviceInfoBinding::inflate) { item, holder, _, _ ->
            holder.bind(item)
        }
    }

}