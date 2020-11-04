package com.crazylegend.vigilante.paging

import androidx.paging.LoadState
import com.crazylegend.vigilante.databinding.ItemviewLoadingStateBinding

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class LoadStateFooter : AbstractLoadStateAdapter<LoadStateViewHolder, ItemviewLoadingStateBinding>(::LoadStateViewHolder, ItemviewLoadingStateBinding::inflate) {
    override fun bindState(loadState: LoadState, holder: LoadStateViewHolder) {
        holder.bindTo(loadState)
    }
}