package com.crazylegend.vigilante.contracts.service

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.crazylegend.coroutines.cancelIfActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by crazy on 10/15/20 to long live and prosper !
 */
interface ServiceCoroutines : ServiceLifecycle {

    var job: Job

    val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val scope get() = CoroutineScope(coroutineContext)

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun createJob() {
        job = Job()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeJob() {
        job.cancelIfActive()
    }


}