package com.crazylegend.vigilante.filter

import android.content.Context
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.recyclerview.AbstractViewBindingHolderAdapter
import com.crazylegend.view.visibleIfTrueGoneOtherwise
import com.crazylegend.vigilante.databinding.ItemviewFilterBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by Hristijan, date 10/13/21
 */
@FragmentScoped
class ListFilterAdapter @Inject constructor() : AbstractViewBindingHolderAdapter<FilterModel, ItemviewFilterBinding>(ItemviewFilterBinding::inflate) {

    override fun bindItems(item: FilterModel, position: Int, itemCount: Int, binding: ItemviewFilterBinding, context: Context) {
        binding.checked.visibleIfTrueGoneOtherwise(item.isChecked)
        binding.title.setPrecomputedText(context.getString(item.title))
        binding.divider.visibleIfTrueGoneOtherwise(position != itemCount - 1)
    }
}