package com.crazylegend.vigilante.headset.ui

import com.crazylegend.vigilante.databinding.ItemviewLogBinding
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.paging.AbstractPagingAdapter
import com.crazylegend.vigilante.utils.LogViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/10/20 to long live and prosper !
 */
@FragmentScoped
class HeadsetAdapter @Inject constructor(
        private val prefsProvider: PrefsProvider
) : AbstractPagingAdapter<HeadsetModel, LogViewHolder, ItemviewLogBinding>(::LogViewHolder, ItemviewLogBinding::inflate) {
    override fun bindItems(item: HeadsetModel?, holder: LogViewHolder, position: Int, itemCount: Int) {
        item ?: return
        holder.bind(item, prefsProvider)
    }
}