package com.crazylegend.vigilante.camera.ui

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
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@AndroidEntryPoint
class CameraAccessFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler), LoadingDBsInFragments, EdgeToEdgeScrolling {

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider

    private val cameraAccessVM by viewModels<CameraAccessVM>()
    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)

    override fun edgeToEdgeScrollingContent() {
        EdgeToEdge.setUpScrollingContent(binding.recycler)
    }

    @Inject
    lateinit var cameraAccessAdapter: CameraAccessAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseLoadingProvider.provideListState(cameraAccessVM.cameraAccess, binding.recycler, binding.noDataViewHolder.noDataView,
                cameraAccessAdapter)
    }
}