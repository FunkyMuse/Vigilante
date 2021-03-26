package com.crazylegend.vigilante.contracts.service

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * Created by crazy on 10/15/20 to long live and prosper !
 */
interface ServiceManagersCoroutines : ServiceLifecycle {

    val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    val scope get() = CoroutineScope(coroutineContext + SupervisorJob())

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeJob() {
        scope.cancel()
    }


}