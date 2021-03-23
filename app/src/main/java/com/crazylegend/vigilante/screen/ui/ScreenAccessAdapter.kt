package com.crazylegend.vigilante.screen.ui

import com.crazylegend.vigilante.databinding.ItemviewLogBinding
import com.crazylegend.vigilante.di.providers.prefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.paging.AbstractPagingAdapter
import com.crazylegend.vigilante.screen.db.ScreenModel
import com.crazylegend.vigilante.utils.LogViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/10/20 to long live and prosper !
 */
@FragmentScoped
class ScreenAccessAdapter @Inject constructor(private val prefsProvider: DefaultPreferencessProvider) : AbstractPagingAdapter<ScreenModel, LogViewHolder, ItemviewLogBinding>(::LogViewHolder, ItemviewLogBinding::inflate) {
    override fun bindItems(item: ScreenModel?, holder: LogViewHolder, position: Int, itemCount: Int) {
        item ?: return
        holder.bind(item, prefsProvider)
    }
}