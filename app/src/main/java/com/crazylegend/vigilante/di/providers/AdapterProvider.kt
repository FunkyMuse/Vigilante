package com.crazylegend.vigilante.di.providers

import com.crazylegend.recyclerview.generateRecycler
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.crashes.CrashViewHolder
import com.crazylegend.vigilante.databinding.*
import com.crazylegend.vigilante.deviceinfo.DeviceInfoModel
import com.crazylegend.vigilante.deviceinfo.DeviceInfoViewHolder
import com.crazylegend.vigilante.filter.FilterModel
import com.crazylegend.vigilante.filter.ListFilterViewHolder
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.home.section.SectionItem
import com.crazylegend.vigilante.home.section.SectionViewHolder
import com.crazylegend.vigilante.microphone.db.MicrophoneModel
import com.crazylegend.vigilante.notifications.db.NotificationsModel
import com.crazylegend.vigilante.paging.generatePagingRecycler
import com.crazylegend.vigilante.permissions.db.PermissionRequestModel
import com.crazylegend.vigilante.power.db.PowerModel
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

    /**
     * just move them into a class where the prefs provider would be a constructor injected into the adapter no the view holder
     * why are you so lazy? :/
     */


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

    val deviceInfoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generateRecycler<DeviceInfoModel, DeviceInfoViewHolder, ItemviewDeviceInfoBinding>(::DeviceInfoViewHolder, ItemviewDeviceInfoBinding::inflate) { item, holder, _, _ ->
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

    val notificationsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generatePagingRecycler<NotificationsModel, LogViewHolder, ItemviewLogBinding>({
            LogViewHolder(it, prefsProvider)
        }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            item?.let { holder.bind(it) }
        }
    }

    val headsetAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generatePagingRecycler<HeadsetModel, LogViewHolder, ItemviewLogBinding>({
            LogViewHolder(it, prefsProvider)
        }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            item?.let { holder.bind(it) }
        }
    }

    val permissionRequestAdapter by lazy {
        generatePagingRecycler<PermissionRequestModel, LogViewHolder, ItemviewLogBinding>({
            LogViewHolder(it, prefsProvider)
        }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            item?.let { holder.bind(it) }
        }
    }

    val powerAdapter by lazy {
        generatePagingRecycler<PowerModel, LogViewHolder, ItemviewLogBinding>({
            LogViewHolder(it, prefsProvider)
        }, ItemviewLogBinding::inflate) { item, holder, _, _ ->
            item?.let { holder.bind(it) }
        }
    }

}