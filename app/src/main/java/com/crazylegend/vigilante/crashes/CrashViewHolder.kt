package com.crazylegend.vigilante.crashes

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.recyclerview.getString
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.databinding.ItemviewCrashBinding

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
class CrashViewHolder(private val binding: ItemviewCrashBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int) {
        binding.text.text = getString(R.string.crash_report, position + 1)
    }
}