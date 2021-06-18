package com.crazylegend.vigilante.power.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.vigilante.paging.PagingProvider
import com.crazylegend.vigilante.power.db.PowerDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
@HiltViewModel
class PowerViewModel @Inject constructor(
        private val powerRepository: PowerDAO,
        pagingProvider: PagingProvider
) : ViewModel() {

    val powerHistory =
        pagingProvider.provideDatabaseData(viewModelScope) { powerRepository.getAllPowerActions() }
}