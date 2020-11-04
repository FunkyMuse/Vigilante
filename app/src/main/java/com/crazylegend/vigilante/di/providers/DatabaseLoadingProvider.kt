package com.crazylegend.vigilante.di.providers

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.coroutines.withMainContext
import com.crazylegend.kotlinextensions.fragments.observeLifecycleOwnerThroughLifecycleCreation
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.views.visibleIfTrueGoneOtherwise
import com.crazylegend.recyclerview.isEmpty
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
class DatabaseLoadingProvider @Inject constructor(private val fragment: Fragment) : LifecycleObserver, LifecycleOwner {


    override fun getLifecycle(): Lifecycle = fragment.viewLifecycleOwner.lifecycle

    init {
        fragment.observeLifecycleOwnerThroughLifecycleCreation {
            lifecycle.addObserver(this@DatabaseLoadingProvider)
        }
    }

    fun <T : Any> provideListState(flow: Flow<PagingData<T>>,
                                   recycler: RecyclerView, noDataView: ConstraintLayout,
                                   adapter: AbstractPagingAdapter<T, *, *>) {

        recycler.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadStateFooter(),
                footer = LoadStateFooter()
        )
        fragment.viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            flow.collect {
                adapter.submitData(it)
            }
        }

        fragment.viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            adapter.loadStateFlow.collectLatest {
                withMainContext {
                    noDataView.visibleIfTrueGoneOtherwise(adapter.isEmpty)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeObserver() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        debug { "ON STARTED" }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        debug { "ON ON_CREATE" }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumed() {
        debug { "ON ON_RESUME" }
    }

}
