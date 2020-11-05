package com.crazylegend.vigilante.permissions.requests.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */

@AndroidEntryPoint
class PermissionRequestsFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler), LoadingDBsInFragments {

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider
    override val binding by viewBinding(LayoutRecyclerBinding::bind)
    private val permissionRequestVM by viewModels<PermissionRequestsVM>()

    private val adapter by lazy {
        adapterProvider.permissionRequestAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseLoadingProvider.provideListState(permissionRequestVM.permissionRequests, binding.recycler, binding.noDataView, adapter)
    }
}