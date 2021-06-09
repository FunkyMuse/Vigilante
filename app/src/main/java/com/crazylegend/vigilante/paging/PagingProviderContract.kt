package com.crazylegend.vigilante.paging

import android.os.Parcelable
import androidx.paging.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Created by funkymuse on 6/9/21 to long live and prosper !
 */
interface PagingProviderContract {

    val getScrollPosition: Parcelable?

    fun saveScrollPos(position: Parcelable?)

    val pagingConfig: PagingConfig get() = PagingConfig(pageSize = 20, enablePlaceholders = false)

    fun <T : Any> provideDatabaseData(
        coroutineScope: CoroutineScope,
        function: () -> PagingSource<Int, T>
    ): Flow<PagingData<T>> =
        Pager(pagingConfig) { function() }.flow.cachedIn(coroutineScope)
}