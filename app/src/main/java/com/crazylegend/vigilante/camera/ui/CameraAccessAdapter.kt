package com.crazylegend.vigilante.camera.ui

import com.crazylegend.vigilante.camera.db.CameraModel
import com.crazylegend.vigilante.databinding.ItemviewLogBinding
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.paging.AbstractPagingAdapter
import com.crazylegend.vigilante.utils.LogViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 11/10/20 to long live and prosper !
 */
@FragmentScoped
class CameraAccessAdapter @Inject constructor(private val prefsProvider: PrefsProvider) :
        AbstractPagingAdapter<CameraModel, LogViewHolder, ItemviewLogBinding>(::LogViewHolder, ItemviewLogBinding::inflate) {
    override fun bindItems(item: CameraModel?, holder: LogViewHolder, position: Int, itemCount: Int) {
        item ?: return
        holder.bind(item, prefsProvider)
    }
}