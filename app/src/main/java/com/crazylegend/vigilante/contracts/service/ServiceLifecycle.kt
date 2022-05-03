package com.crazylegend.vigilante.contracts.service

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * Created by crazy on 10/16/20 to long live and prosper !
 */
interface ServiceLifecycle : LifecycleOwner, LifecycleEventObserver {

    val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate

    val scope: CoroutineScope
        get() = CoroutineScope(coroutineContext + SupervisorJob())

    val serviceLifecycleDispatcher: ServiceLifecycleDispatcher
    override fun getLifecycle(): Lifecycle = serviceLifecycleDispatcher.lifecycle

    fun initLifecycle() {
        serviceLifecycleDispatcher.onServicePreSuperOnCreate()
        serviceLifecycleDispatcher.lifecycle.addObserver(this)
    }

    fun setServiceConnected() {
        serviceLifecycleDispatcher.onServicePreSuperOnStart()
    }

    fun cleanUp() {
        serviceLifecycleDispatcher.onServicePreSuperOnDestroy()
    }

    fun onStart() {
        serviceLifecycleDispatcher.onServicePreSuperOnStart()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> initVars()
            Lifecycle.Event.ON_START -> registerCallbacks()
            Lifecycle.Event.ON_DESTROY -> {
                disposeResources()
                scope.cancel()
            }
            else -> {
            }
        }
    }

    fun initVars()

    fun registerCallbacks()

    fun disposeResources()
}