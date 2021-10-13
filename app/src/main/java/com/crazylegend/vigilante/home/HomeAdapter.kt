package com.crazylegend.vigilante.home

import android.content.Context
import androidx.core.view.isVisible
import com.crazylegend.recyclerview.AbstractViewBindingHolderAdapter
import com.crazylegend.vigilante.databinding.ItemviewSectionBinding
import com.crazylegend.vigilante.home.section.SectionItem
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by Hristijan, date 10/13/21
 */
@FragmentScoped
class HomeAdapter @Inject constructor() : AbstractViewBindingHolderAdapter<SectionItem, ItemviewSectionBinding>(ItemviewSectionBinding::inflate) {

    override fun bindItems(item: SectionItem, position: Int, itemCount: Int, binding: ItemviewSectionBinding, context: Context) {
        binding.title.text = context.getString(item.title)
        binding.icon.setImageResource(item.icon)
        binding.rightDivider.isVisible = position % 2 == 0
        binding.bottomDivider.isVisible = (itemCount - 1 != position) && (itemCount - 2 != position)
    }
}