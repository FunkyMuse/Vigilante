package com.crazylegend.vigilante.permissions.ui.request

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.fragments.viewCoroutineScope
import com.crazylegend.kotlinextensions.views.hideViews
import com.crazylegend.kotlinextensions.views.showViews
import com.crazylegend.navigation.navigateSafe
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.EdgeToEdgeScrolling
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.FragmentPermissionsBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import com.crazylegend.vigilante.utils.EdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */

@AndroidEntryPoint
class PermissionRequestsFragment : AbstractFragment<FragmentPermissionsBinding>(R.layout.fragment_permissions), LoadingDBsInFragments, EdgeToEdgeScrolling {

    override fun edgeToEdgeScrollingContent() {
        EdgeToEdge.setUpScrollingContent(binding.recycler)
    }

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider
    override val binding by viewBinding(FragmentPermissionsBinding::bind)
    private val permissionRequestVM by viewModels<PermissionRequestsViewModel>()

    @Inject
    lateinit var adapter: PermissionRequestsAdapter

    private val viewProneToVisibilityChange: Array<View>
        get() = arrayOf(
                binding.totalPermissionRequests, binding.totalPermissionRequestsTitle
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseLoadingProvider.provideListState(permissionRequestVM.permissionRequests, binding.recycler, binding.noDataViewHolder.noDataView, adapter) {
            if (it) {
                hideViews(*viewProneToVisibilityChange)
            } else {
                showViews(*viewProneToVisibilityChange)
            }
        }
        viewCoroutineScope.launchWhenResumed {
            permissionRequestVM.totalPermissionRequests.collectLatest {
                binding.totalPermissionRequests.text = it.toString()
            }
        }
        adapter.forItemClickListener = forItemClickListener { _, item, _ ->
            item.packageRequestingThePermission?.let {
                findNavController().navigateSafe(PermissionRequestsFragmentDirections.destinationPermissionDetails(it,
                        item.permissionMessage, item.date.time, item.settingsAppName))
            }
        }
    }
}