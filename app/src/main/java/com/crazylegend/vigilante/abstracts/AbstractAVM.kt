package com.crazylegend.vigilante.abstracts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
abstract class AbstractAVM(application: Application) : AndroidViewModel(application) {

    @PublishedApi
    internal val pagingConfig = PagingConfig(pageSize = 20, enablePlaceholders = false)

    inline fun <T : Any> provideDatabaseData(crossinline function: () -> PagingSource<Int, T>): Flow<PagingData<T>> {
        return Pager(pagingConfig) {
            function()
        }.flow.cachedIn(viewModelScope)
    }
}