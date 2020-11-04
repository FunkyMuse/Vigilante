package com.crazylegend.vigilante.paging

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadState.Loading
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.vigilante.databinding.ItemviewLoadingStateBinding

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class LoadStateViewHolder(binding: ItemviewLoadingStateBinding) : RecyclerView.ViewHolder(binding.root) {
    private val progressBar = binding.progressBar

    fun bindTo(loadState: LoadState) {
        progressBar.isVisible = loadState is Loading
    }
}
