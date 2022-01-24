package com.crazylegend.vigilante.screen.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.crazylegend.fragment.fragmentIntResult
import com.crazylegend.lifecycle.repeatingJobOnStarted
import com.crazylegend.view.setOnClickListenerCooldown
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.FragmentScreenAccessBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import com.crazylegend.vigilante.filter.ListFilterBottomSheet
import com.crazylegend.vigilante.utils.goToScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@AndroidEntryPoint
class ScreenAccessFragment : AbstractFragment<FragmentScreenAccessBinding>(R.layout.fragment_screen_access), LoadingDBsInFragments {


    override val binding: FragmentScreenAccessBinding by viewBinding(FragmentScreenAccessBinding::bind)

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider

    @Inject
    lateinit var adapter: ScreenAccessAdapter

    private val screenVM by viewModels<ScreenViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideAdapterDataByFilterOrDefault()

        fragmentIntResult(ListFilterBottomSheet.RESULT_REQUEST_KEY, ListFilterBottomSheet.BUNDLE_ARGUMENT_KEy) {
            val result = it ?: return@fragmentIntResult
            screenVM.updateFilterPosition(result)
            provideAdapterDataByFilterOrDefault()
        }

        binding.filter.setOnClickListenerCooldown {
            goToScreen(ScreenAccessFragmentDirections.actionFilter(screenVM.getFilterList()))
        }

        repeatingJobOnStarted {
            screenVM.totalActions.collect {
                binding.totalActions.text = it.toString()
            }
        }

        repeatingJobOnStarted {
            screenVM.totalLocks.collect {
                binding.totalLocks.text = it.toString()
            }
        }
        repeatingJobOnStarted {
            screenVM.totalUnlocks.collect {
                binding.totalUnlocks.text = it.toString()
            }
        }
    }

    private fun provideAdapterDataByFilterOrDefault() {
        databaseLoadingProvider.provideListState(screenVM.screenAccessData, binding.recycler, binding.noDataViewHolder.noDataView, adapter) { viewsVisibility ->
            binding.viewsProneToVisibilityChange.isGone = viewsVisibility
        }
    }
}