package com.crazylegend.vigilante.power.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.power.db.PowerRepository

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
class PowerVM @ViewModelInject constructor(private val powerRepository: PowerRepository, application: Application) : AbstractAVM(application) {

    val powerHistory = provideDatabaseData { powerRepository.getAllPowerActions() }
}