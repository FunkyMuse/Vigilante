package com.crazylegend.vigilante.power.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.database.coroutines.dbCallStateFlow
import com.crazylegend.vigilante.power.db.PowerDAO
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class PowerDetailsViewModel @AssistedInject constructor(
        @Assisted private val powerID: Int,
        private val powerRepository: PowerDAO) : ViewModel() {

    @AssistedFactory
    interface PowerDetailsVMFactory {
        fun create(powerID: Int): PowerDetailsViewModel
    }

    val powerModel = viewModelScope.dbCallStateFlow { powerRepository.getPowerActionByID(powerID) }
}