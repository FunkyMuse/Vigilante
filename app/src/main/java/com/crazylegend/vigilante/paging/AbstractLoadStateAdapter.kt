package com.crazylegend.vigilante.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
abstract class AbstractLoadStateAdapter<VH : RecyclerView.ViewHolder, VB : ViewBinding>(
        private val viewHolder: (binding: VB) -> VH,
        private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB
) : LoadStateAdapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): VH =
            setViewHolder(bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: VH, loadState: LoadState) {
        bindState(loadState, holder)
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean = true

    abstract fun bindState(loadState: LoadState, holder: VH)

    @Suppress("UNCHECKED_CAST")
    private fun setViewHolder(binding: ViewBinding): VH = viewHolder(binding as VB)
}

inline fun <VH : RecyclerView.ViewHolder, VB : ViewBinding> generateLoadStateAdapter(
        noinline viewHolder: (binding: VB) -> VH,
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        crossinline onState: (LoadState) -> Unit): AbstractLoadStateAdapter<VH, VB> {

    return object : AbstractLoadStateAdapter<VH, VB>(viewHolder, bindingInflater) {
        override fun bindState(loadState: LoadState, holder: VH) {
            onState(loadState)
        }
    }
}