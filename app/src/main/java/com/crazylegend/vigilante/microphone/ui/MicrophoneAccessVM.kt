package com.crazylegend.vigilante.microphone.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.microphone.db.MicrophoneRepository

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class MicrophoneAccessVM @ViewModelInject constructor(
        private val microphoneRepository: MicrophoneRepository,
        application: Application) : AbstractAVM(application) {

    val microphoneAccess = Pager(pagingConfig) {
        microphoneRepository.getAllMicrophoneRecords()
    }.flow.cachedIn(viewModelScope)
}