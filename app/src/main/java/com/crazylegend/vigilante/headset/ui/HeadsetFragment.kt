package com.crazylegend.vigilante.headset.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.EdgeToEdgeScrolling
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import com.crazylegend.vigilante.utils.EdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@AndroidEntryPoint
class HeadsetFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler), LoadingDBsInFragments, EdgeToEdgeScrolling {

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider
    override val binding by viewBinding(LayoutRecyclerBinding::bind)

    @Inject
    lateinit var adapter: HeadsetAdapter

    override fun edgeToEdgeScrollingContent() {
        EdgeToEdge.setUpScrollingContent(binding.recycler)
    }

    private val headsetVM by viewModels<HeadsetViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseLoadingProvider.provideListState(headsetVM.headsetData, binding.recycler, binding.noDataViewHolder.noDataView, adapter)
    }
}