package com.crazylegend.vigilante.headset.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.vigilante.headset.database.HeadsetRepository
import com.crazylegend.vigilante.paging.PagingProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@HiltViewModel
class HeadsetViewModel @Inject constructor(
    private val repo: HeadsetRepository,
    pagingProvider: PagingProvider
) : ViewModel() {

    val headsetData =
        pagingProvider.provideDatabaseData(viewModelScope) { repo.getAllHeadsetRecords() }

}