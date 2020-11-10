package com.crazylegend.vigilante.screen.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.fragments.fragmentIntResult
import com.crazylegend.kotlinextensions.fragments.viewLifecycleOwnerLifecycle
import com.crazylegend.kotlinextensions.views.hideViews
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.showViews
import com.crazylegend.navigation.navigateSafe
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.EdgeToEdgeScrolling
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.FragmentScreenAccessBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import com.crazylegend.vigilante.filter.ListFilterBottomSheet
import com.crazylegend.vigilante.utils.EdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@AndroidEntryPoint
class ScreenAccessFragment : AbstractFragment<FragmentScreenAccessBinding>(R.layout.fragment_screen_access), LoadingDBsInFragments, EdgeToEdgeScrolling {

    override fun edgeToEdgeScrollingContent() {
        EdgeToEdge.setUpScrollingContent(binding.recycler)
    }

    override val binding: FragmentScreenAccessBinding by viewBinding(FragmentScreenAccessBinding::bind)

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider

    @Inject
    lateinit var adapter: ScreenAccessAdapter

    private val screenVM by viewModels<ScreenVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideAdapterDataByFilterOrDefault()

        fragmentIntResult(ListFilterBottomSheet.RESULT_REQUEST_KEY, ListFilterBottomSheet.BUNDLE_ARGUMENT_KEy) {
            this ?: return@fragmentIntResult
            screenVM.updateFilterPosition(this)
            provideAdapterDataByFilterOrDefault()
        }

        binding.filter.setOnClickListenerCooldown {
            findNavController().navigateSafe(ScreenAccessFragmentDirections.actionFilter(screenVM.getFilterList()))
        }

        viewLifecycleOwnerLifecycle.coroutineScope.launchWhenResumed {
            screenVM.totalActions.collect {
                binding.totalActions.text = it.toString()
            }
        }

        viewLifecycleOwnerLifecycle.coroutineScope.launchWhenResumed {
            screenVM.totalLocks.collect {
                binding.totalLocks.text = it.toString()
            }
        }

        viewLifecycleOwnerLifecycle.coroutineScope.launchWhenResumed {
            screenVM.totalUnlocks.collect {
                binding.totalUnlocks.text = it.toString()
            }
        }
    }

    private val viewProneToVisibilityChange: Array<View>
        get() = arrayOf(
                binding.filter, binding.totalActions, binding.totalActionsTitle, binding.totalLocks, binding.totalLocksTitle,
                binding.totalUnlocks, binding.totalUnlocksTitle
        )

    private fun provideAdapterDataByFilterOrDefault() {
        databaseLoadingProvider.provideListState(screenVM.screenAccessData, binding.recycler, binding.noDataViewHolder.noDataView, adapter) {
            if (it) {
                hideViews(*viewProneToVisibilityChange)
            } else {
                showViews(*viewProneToVisibilityChange)
            }
        }
    }
}