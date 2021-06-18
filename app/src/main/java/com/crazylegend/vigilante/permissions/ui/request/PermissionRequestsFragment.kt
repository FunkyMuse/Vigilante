package com.crazylegend.vigilante.permissions.ui.request

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.crazylegend.lifecycle.repeatingJobOnStarted
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.FragmentPermissionsBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import com.crazylegend.vigilante.utils.goToScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */

@AndroidEntryPoint
class PermissionRequestsFragment : AbstractFragment<FragmentPermissionsBinding>(R.layout.fragment_permissions), LoadingDBsInFragments {


    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider
    override val binding by viewBinding(FragmentPermissionsBinding::bind)
    private val permissionRequestVM by viewModels<PermissionRequestsViewModel>()

    @Inject
    lateinit var adapter: PermissionRequestsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseLoadingProvider.provideListState(permissionRequestVM.permissionRequests, binding.recycler, binding.noDataViewHolder.noDataView, adapter) { viewsVisibility ->
            binding.viewsProneToVisibilityChange.isGone = viewsVisibility
        }
        repeatingJobOnStarted {
            permissionRequestVM.totalPermissionRequests.collectLatest {
                binding.totalPermissionRequests.text = it.toString()
            }
        }
        adapter.forItemClickListener = forItemClickListener { _, item, _ ->
            item.packageRequestingThePermission?.let {
                goToScreen(PermissionRequestsFragmentDirections.destinationPermissionDetails(it,
                        item.permissionMessage, item.date.time, item.settingsAppName))
            }
        }
    }
}