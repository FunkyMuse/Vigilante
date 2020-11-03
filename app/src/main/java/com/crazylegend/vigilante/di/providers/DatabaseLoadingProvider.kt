package com.crazylegend.vigilante.di.providers

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.customviews.ui.ColorProgressBar
import com.crazylegend.database.DBResult
import com.crazylegend.database.handle
import com.crazylegend.kotlinextensions.fragments.observeLifecycleOwnerThroughLifecycleCreation
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visibleIfTrueGoneOtherwise
import com.crazylegend.recyclerview.AbstractViewBindingAdapter
import com.crazylegend.recyclerview.isEmpty
import dagger.hilt.android.scopes.FragmentScoped
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

    fun <T> provideListState(liveData: LiveData<DBResult<List<T>>>,
                             recycler: RecyclerView, noDataView: ConstraintLayout,
                             adapter: AbstractViewBindingAdapter<T, *, *>, loadingIndicator: ColorProgressBar) {

        recycler.adapter = adapter
        liveData.observe(this) {
            loadingIndicator.visibleIfTrueGoneOtherwise(it is DBResult.Querying)
            it.handle(
                    queryingDB = {
                        noDataView.gone()
                    },
                    emptyDB = {
                        noDataView.visibleIfTrueGoneOtherwise(adapter.isEmpty)
                    },
                    dbError = { throwable ->
                        throwable.printStackTrace()
                        noDataView.visibleIfTrueGoneOtherwise(adapter.isEmpty)
                    },
                    success = {
                        noDataView.visibleIfTrueGoneOtherwise(adapter.isEmpty && isNullOrEmpty())
                        adapter.submitList(this)
                    }
            )
        }
    }


}
