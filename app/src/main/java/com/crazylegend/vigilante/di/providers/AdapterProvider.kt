package com.crazylegend.vigilante.di.providers

import com.crazylegend.recyclerview.generateRecycler
import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.camera.ui.CameraAccessViewHolder
import com.crazylegend.vigilante.crashes.CrashViewHolder
import com.crazylegend.vigilante.databinding.ItemviewCameraAccessBinding
import com.crazylegend.vigilante.databinding.ItemviewCrashBinding
import com.crazylegend.vigilante.databinding.ItemviewSectionBinding
import com.crazylegend.vigilante.home.section.SectionItem
import com.crazylegend.vigilante.home.section.SectionViewHolder
import com.crazylegend.vigilante.microphone.db.MicrophoneModel
import com.crazylegend.vigilante.microphone.ui.MicrophoneAccessViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@FragmentScoped
class AdapterProvider @Inject constructor(
        private val prefsProvider: PrefsProvider
) {

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
        generateRecycler<CameraModel, CameraAccessViewHolder, ItemviewCameraAccessBinding>({
            CameraAccessViewHolder(it, prefsProvider)
        }, ItemviewCameraAccessBinding::inflate) { item, holder, _, _ ->
            holder.bind(item)
        }
    }

    val micAccessAdapter by lazy(LazyThreadSafetyMode.NONE) {
        generateRecycler<MicrophoneModel, MicrophoneAccessViewHolder, ItemviewCameraAccessBinding>({
            MicrophoneAccessViewHolder(it, prefsProvider)
        }, ItemviewCameraAccessBinding::inflate) { item, holder, _, _ ->
            holder.bind(item)
        }
    }

}