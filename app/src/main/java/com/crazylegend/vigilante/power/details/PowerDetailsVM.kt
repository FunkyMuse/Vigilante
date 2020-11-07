package com.crazylegend.vigilante.power.details

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.crazylegend.database.coroutines.makeDBCallLiveData
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.power.db.PowerRepository

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class PowerDetailsVM @ViewModelInject constructor(
        @Assisted private val savedStateHandle: SavedStateHandle,
        private val powerRepository: PowerRepository,
        application: Application) : AbstractAVM(application) {

    private val powerID get() = savedStateHandle.get<Int>("powerID") ?: -1

    val powerModel = makeDBCallLiveData { powerRepository.getPowerModelById(powerID) }
}