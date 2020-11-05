package com.crazylegend.vigilante.screen.ui

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
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

    companion object {
        private const val FILTER_MODEL_KEY = "filterModel"
    }

    private val filterPosition get() = savedStateHandle.get<Int>(FILTER_MODEL_KEY) ?: 0

    fun updateFilterPosition(position: Int) {
        savedStateHandle[FILTER_MODEL_KEY] = position
    }

    val screenAccessData
        get() = when (filterPosition) {
            0 -> allScreenAccess
            1 -> allScreenLocks
            2 -> allScreenUnLocks
            else -> allScreenAccess
        }


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

    private val allScreenAccess = provideDatabaseData { repo.getAllScreenActions() }

    private val allScreenLocks = provideDatabaseData { repo.getAllScreenLocks() }

    private val allScreenUnLocks = provideDatabaseData { repo.getAllScreenUnlocks() }

}