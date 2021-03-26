package com.crazylegend.vigilante.di.providers

import androidx.core.view.isVisible
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.visibleIfTrueGoneOtherwise
import com.crazylegend.recyclerview.generateRecyclerWithHolder
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.databinding.ItemviewCrashBinding
import com.crazylegend.vigilante.databinding.ItemviewDeviceInfoBinding
import com.crazylegend.vigilante.databinding.ItemviewFilterBinding
import com.crazylegend.vigilante.databinding.ItemviewSectionBinding
import com.crazylegend.vigilante.deviceinfo.DeviceInfoModel
import com.crazylegend.vigilante.filter.FilterModel
import com.crazylegend.vigilante.home.section.SectionItem
import com.crazylegend.vigilante.utils.lazyNonSynchronized
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@FragmentScoped
class AdapterProvider @Inject constructor() {

    val listFilterAdapter by lazyNonSynchronized {
        generateRecyclerWithHolder<FilterModel, ItemviewFilterBinding>(ItemviewFilterBinding::inflate) { item, position, itemCount, binding, context ->
            binding.checked.visibleIfTrueGoneOtherwise(item.isChecked)
            binding.title.setPrecomputedText(context.getString(item.title))
            binding.divider.visibleIfTrueGoneOtherwise(position != itemCount - 1)
        }
    }

    val sectionAdapter by lazyNonSynchronized {
        generateRecyclerWithHolder<SectionItem, ItemviewSectionBinding>(ItemviewSectionBinding::inflate) { item, position, itemCount, binding, context ->
            binding.title.text = context.getString(item.title)
            binding.icon.setImageResource(item.icon)
            binding.rightDivider.isVisible = position % 2 == 0
            binding.bottomDivider.isVisible = (itemCount - 1 != position) && (itemCount - 2 != position)
        }
    }

    val crashesAdapter by lazyNonSynchronized {
        generateRecyclerWithHolder<String, ItemviewCrashBinding>(ItemviewCrashBinding::inflate) { _, position, _, binding, context ->
            binding.text.text = context.getString(R.string.crash_report, position + 1)
        }
    }

    val deviceInfoAdapter by lazyNonSynchronized {
        generateRecyclerWithHolder<DeviceInfoModel, ItemviewDeviceInfoBinding>(ItemviewDeviceInfoBinding::inflate) { item, _, _, binding, context ->
            binding.title.setPrecomputedText(context.getString(item.title))
            binding.content.setPrecomputedText(context.getString(item.content))
        }
    }

}