package com.crazylegend.vigilante.filter

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.visibleIfTrueGoneOtherwise
import com.crazylegend.recyclerview.getString
import com.crazylegend.vigilante.databinding.ItemviewFilterBinding

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class ListFilterViewHolder(private val binding: ItemviewFilterBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FilterModel, position: Int, itemCount: Int) {
        binding.checked.visibleIfTrueGoneOtherwise(item.isChecked)
        binding.title.setPrecomputedText(getString(item.title))
        binding.divider.visibleIfTrueGoneOtherwise(position != itemCount - 1)
    }

}