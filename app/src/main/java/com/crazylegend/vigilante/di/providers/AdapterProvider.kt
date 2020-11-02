package com.crazylegend.vigilante.di.providers

import com.crazylegend.recyclerview.generateRecycler
import com.crazylegend.vigilante.crashes.CrashViewHolder
import com.crazylegend.vigilante.databinding.ItemviewCrashBinding
import com.crazylegend.vigilante.databinding.ItemviewSectionsBinding
import com.crazylegend.vigilante.home.section.SectionItem
import com.crazylegend.vigilante.home.section.SectionViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@FragmentScoped
class AdapterProvider @Inject constructor() {

    val sectionAdapter by lazy {
        generateRecycler<SectionItem, SectionViewHolder, ItemviewSectionsBinding>(::SectionViewHolder, ItemviewSectionsBinding::inflate) { item, holder, position, itemCount ->
            holder.bind(item, position, itemCount)
        }
    }

    val crashesAdapter by lazy {
        generateRecycler<String, CrashViewHolder, ItemviewCrashBinding>(::CrashViewHolder, ItemviewCrashBinding::inflate) { item, holder, position, itemCount ->
            holder.bind(position)
        }
    }

}