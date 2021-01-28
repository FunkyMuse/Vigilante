package com.crazylegend.vigilante.power.details

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.crazylegend.database.coroutines.dbCallStateFlow
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.power.db.PowerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class PowerDetailsVM @AssistedInject constructor(
        @Assisted private val powerID: Int,
        private val powerRepository: PowerRepository,
        application: Application) : AbstractAVM(application) {

    @AssistedFactory
    interface PowerDetailsVMFactory {
        fun create(powerID: Int): PowerDetailsVM
    }

    val powerModel = viewModelScope.dbCallStateFlow { powerRepository.getPowerActionByID(powerID) }
}