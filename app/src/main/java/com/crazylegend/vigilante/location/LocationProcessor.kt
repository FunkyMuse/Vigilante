package com.crazylegend.vigilante.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.crazylegend.common.ifTrue
import com.crazylegend.contextgetters.locationManager
import com.crazylegend.contextgetters.notificationManager
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.contracts.service.ServiceLifecycle
import com.crazylegend.vigilante.di.providers.UserNotificationsProvider
import com.crazylegend.vigilante.di.providers.prefs.location.LocationPrefs
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.service.VigilanteService
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by funkymuse on 6/10/21 to long live and prosper !
 */
@ServiceScoped
class LocationProcessor @Inject constructor(
        @ServiceContext private val context: Context,
        private val userNotificationsProvider: UserNotificationsProvider,
        private val locationPrefs: LocationPrefs) : ServiceLifecycle {

    private companion object {
        private const val locationNotificationID = 420
    }

    private val locationStatusData = MutableStateFlow(false)
    private val locationStatus = locationStatusData.asStateFlow().shareIn(scope, SharingStarted.WhileSubscribed())

    override val serviceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    private lateinit var locationStatusReceiver: BroadcastReceiver

    override fun initVars() {
        locationStatusReceiver = initLocationStatusReceiver()

        scope.launch {
            locationStatus.collectLatest {
                if (it) {
                    setLocationIsUsed()
                    VigilanteService.serviceLayoutListener?.showLocation()
                } else {
                    stopNotificationIfUserEnabled()
                    VigilanteService.serviceLayoutListener?.hideLocation()
                }
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
                }
            }
        }
    }

    private fun sendNotificationIfUserEnabled() {
        locationPrefs.areNotificationsEnabled.ifTrue {
            userNotificationsProvider.buildUsageNotification(locationNotificationID, R.string.location_being_used, locationPrefs.getLocationNotificationLEDColorPref,
                    locationPrefs.getLocationVibrationEffectPref, locationPrefs.isBypassDNDEnabled, locationPrefs.isSoundEnabled)
        }
    }

    private fun stopNotificationIfUserEnabled() {
        context.notificationManager?.cancel(locationNotificationID)
    }

    private fun setLocationIsUsed() {
        sendNotificationIfUserEnabled()
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