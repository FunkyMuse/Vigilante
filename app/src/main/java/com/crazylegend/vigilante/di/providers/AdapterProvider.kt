package com.crazylegend.vigilante.di.providers

import android.app.usage.UsageStats
import com.crazylegend.recyclerview.generateRecycler
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.crashes.CrashViewHolder
import com.crazylegend.vigilante.databinding.ItemviewCrashBinding
import com.crazylegend.vigilante.databinding.ItemviewFilterBinding
import com.crazylegend.vigilante.databinding.ItemviewLogBinding
import com.crazylegend.vigilante.databinding.ItemviewSectionBinding
import com.crazylegend.vigilante.filter.FilterModel
import com.crazylegend.vigilante.filter.ListFilterViewHolder
import com.crazylegend.vigilante.home.section.SectionItem
import com.crazylegend.vigilante.home.section.SectionViewHolder
import com.crazylegend.vigilante.microphone.db.MicrophoneModel
import com.crazylegend.vigilante.paging.generatePagingRecycler
import com.crazylegend.vigilante.screen.db.ScreenModel
import com.crazylegend.vigilante.utils.LogViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@FragmentScoped
class AdapterProvider @Inject constructor(
        private val prefsProvider: PrefsProvider
) {

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

    val cameraAccessAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generatePagingRecycler<CameraModel, LogViewHolder, ItemviewLogBinding>({
            LogViewHolder(it, prefsProvider)
        }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            item?.let { holder.bind(it) }
        }
    }

    val micAccessAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generatePagingRecycler<MicrophoneModel, LogViewHolder, ItemviewLogBinding>({
            LogViewHolder(it, prefsProvider)
        }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            item?.let { holder.bind(it) }
        }
    }

    val appsUsageAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generateRecycler<UsageStats, LogViewHolder, ItemviewLogBinding>({ LogViewHolder(it, prefsProvider) }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            holder.bind(item)
        }
    }

    val screenAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generatePagingRecycler<ScreenModel, LogViewHolder, ItemviewLogBinding>({
            LogViewHolder(it, prefsProvider)
        }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            item?.let { holder.bind(it) }
        }
    }
}