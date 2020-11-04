package com.crazylegend.vigilante.screen.ui

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.filter.FilterModel
import com.crazylegend.vigilante.screen.db.ScreenRepository

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class ScreenVM @ViewModelInject constructor(
        private val repo: ScreenRepository,
        application: Application,
        @Assisted private val savedStateHandle: SavedStateHandle) : AbstractAVM(application) {

    fun updateFilterPosition(position: Int) {
        savedStateHandle[FILTER_MODEL_KEY] = position
    }

    companion object {
        const val FILTER_MODEL_KEY = "filterModel"
    }

    val filterPosition get() = savedStateHandle.get<Int>(FILTER_MODEL_KEY) ?: 0


    fun getFilterList(): Array<FilterModel> {
        return arrayOf(
                FilterModel(R.string.locks_and_unlocks),
                FilterModel(R.string.locks_only),
                FilterModel(R.string.unlocks_only),
        ).also {
            it[filterPosition].isChecked = true
        }
    }


    val totalActions get() = repo.totalActions()
    val totalLocks get() = repo.totalLocks()
    val totalUnlocks get() = repo.totalUnlocks()

    val allScreenAccess = Pager(pagingConfig) {
        repo.getAllScreenActions()
    }.flow.cachedIn(viewModelScope)

    val allScreenLocks = Pager(pagingConfig) {
        repo.getAllScreenLocks()
    }.flow.cachedIn(viewModelScope)
    val allScreenUnLocks = Pager(pagingConfig) {
        repo.getAllScreenUnlocks()
    }.flow.cachedIn(viewModelScope)

}