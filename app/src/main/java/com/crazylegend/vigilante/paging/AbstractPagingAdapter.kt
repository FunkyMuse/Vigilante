package com.crazylegend.vigilante.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.recyclerview.GenericDiffUtil
import com.crazylegend.recyclerview.clickListeners.forItemClickListener

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
abstract class AbstractPagingAdapter<T : Any, VH : RecyclerView.ViewHolder, VB : ViewBinding>(
        private val viewHolder: (binding: VB) -> VH,
        private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null }
) :
        PagingDataAdapter<T, VH>(GenericDiffUtil(areItemsTheSameCallback, areContentsTheSameCallback)) {
    abstract fun bindItems(item: T?, holder: VH, position: Int, itemCount: Int)

    var forItemClickListener: forItemClickListener<T>? = null
    var onLongClickListener: forItemClickListener<T>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T? = getItem(position)
        bindItems(item, holder, position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        val holder = setViewHolder(binding)

        holder.itemView.setOnClickListenerCooldown {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION)
                getItem(holder.bindingAdapterPosition)?.let { item -> forItemClickListener?.forItem(holder.bindingAdapterPosition, item, it) }
        }
        holder.itemView.setOnLongClickListener {
            if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION)
                getItem(holder.bindingAdapterPosition)?.let { item -> onLongClickListener?.forItem(holder.bindingAdapterPosition, item, it) }
            true
        }
        return holder
    }

    @Suppress("UNCHECKED_CAST")
    private fun setViewHolder(binding: ViewBinding): VH = viewHolder(binding as VB)

}

inline fun <reified T : Any, VH : RecyclerView.ViewHolder, VB : ViewBinding> generatePagingRecycler(
        noinline viewHolder: (binding: VB) -> VH,
        noinline bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VB,
        noinline areItemsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        noinline areContentsTheSameCallback: (old: T, new: T) -> Boolean? = { _, _ -> null },
        crossinline binder: (item: T?, holder: VH, position: Int, itemCount: Int) -> Unit): AbstractPagingAdapter<T, VH, VB> {

    return object : AbstractPagingAdapter<T, VH, VB>(viewHolder, bindingInflater, areItemsTheSameCallback, areContentsTheSameCallback) {
        override fun bindItems(item: T?, holder: VH, position: Int, itemCount: Int) {
            binder(item, holder, position, itemCount)
        }
    }
}