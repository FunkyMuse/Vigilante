package com.crazylegend.vigilante.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.kotlinextensions.context.deviceNetworkType
import com.crazylegend.kotlinextensions.context.locationManager
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.vigilante.contracts.service.ServiceManagersCoroutines
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by funkymuse on 6/10/21 to long live and prosper !
 */
@ServiceScoped
class LocationProcessor @Inject constructor(@ServiceContext private val context: Context) :
    ServiceManagersCoroutines {

    private val locationStatusData = MutableStateFlow(false)
    private val locationStatus =
        locationStatusData.asStateFlow().shareIn(scope, SharingStarted.WhileSubscribed())

    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    private lateinit var locationStatusReceiver: BroadcastReceiver

    override fun initVars() {
        locationStatusReceiver = initLocationStatusReceiver()

        scope.launch {
            locationStatus.collectLatest {
                debug { "LOCATION STATUS $it" }
            }
        }
    }

    private fun initLocationStatusReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent ?: return
                context ?: return
                if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                    val locationManager = context.locationManager ?: return
                    val isGpsEnabled =
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    val isNetworkEnabled =
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    locationStatusData.value = isGpsEnabled || isNetworkEnabled
                    context.deviceNetworkType()
                }
            }
        }
    }

    override fun registerCallbacks() {
        val filter = IntentFilter().apply {
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        }
        context.registerReceiver(locationStatusReceiver, filter)
    }

    override fun disposeResources() {
        context.unregisterReceiver(locationStatusReceiver)
    }
}