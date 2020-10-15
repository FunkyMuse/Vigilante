package com.crazylegend.vigilante.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by crazy on 10/15/20 to long live and prosper !
 */
interface ServiceCoroutines {
    var job: Job
    val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val scope get() = CoroutineScope(coroutineContext)
}