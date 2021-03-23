package com.crazylegend.vigilante.crashes.details

import androidx.lifecycle.ViewModel
import com.crazylegend.crashyreporter.CrashyReporter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by crazy on 1/28/21 to long live and prosper !
 */

class DetailedCrashViewModel @AssistedInject constructor(@Assisted private val position: Int) : ViewModel() {

    @AssistedFactory
    interface DetailedCrashVMFactory {
        fun create(position: Int): DetailedCrashViewModel
    }

    val detailedCrash = CrashyReporter.getLogsAsStrings()?.getOrNull(position)
}