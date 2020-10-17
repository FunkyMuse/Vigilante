package com.crazylegend.vigilante.contracts.service

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher

/**
 * Created by crazy on 10/16/20 to long live and prosper !
 */
interface ServiceLifecycle : LifecycleOwner, LifecycleObserver, LifecycleProviderContract {

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
}