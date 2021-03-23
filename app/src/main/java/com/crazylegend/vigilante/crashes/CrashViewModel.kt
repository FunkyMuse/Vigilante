package com.crazylegend.vigilante.crashes

import androidx.lifecycle.ViewModel
import com.crazylegend.crashyreporter.CrashyReporter

/**
 * Created by crazy on 1/28/21 to long live and prosper !
 */
class CrashViewModel : ViewModel() {

    val crashes = CrashyReporter.getLogsAsStrings()
}