package com.crazylegend.vigilante.home.section

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.recyclerview.getString
import com.crazylegend.vigilante.databinding.ItemviewSectionsBinding

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
class SectionViewHolder(private val binding: ItemviewSectionsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SectionItem, position: Int, itemCount: Int) {
        binding.title.text = getString(item.title)
        binding.icon.setImageResource(item.icon)
        binding.rightDivider.isVisible = position % 2 == 0
        binding.bottomDivider.isVisible = (itemCount - 1 != position) && (itemCount - 2 != position)
    }
}