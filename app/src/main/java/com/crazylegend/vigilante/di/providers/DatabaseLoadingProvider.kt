package com.crazylegend.vigilante.di.providers

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.lifecycle.repeatingJobOnStarted
import com.crazylegend.recyclerview.isEmpty
import com.crazylegend.view.visibleIfTrueGoneOtherwise
import com.crazylegend.vigilante.paging.AbstractPagingAdapter
import com.crazylegend.vigilante.paging.LoadStateFooter
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@FragmentScoped
class DatabaseLoadingProvider @Inject constructor(private val fragment: Fragment) {

    fun <T : Any> provideListState(flow: Flow<PagingData<T>>,
                                   recycler: RecyclerView, noDataView: ConstraintLayout,
                                   adapter: AbstractPagingAdapter<T, *, *>, onAdapterCount: (Boolean) -> Unit = {}) {

        recycler.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadStateFooter(),
                footer = LoadStateFooter()
        )
        fragment.repeatingJobOnStarted { flow.collect { adapter.submitData(it) } }
        fragment.repeatingJobOnStarted {
            adapter.loadStateFlow.collectLatest {
                noDataView.visibleIfTrueGoneOtherwise(adapter.isEmpty)
                onAdapterCount(adapter.isEmpty)
            }
        }
    }
}
