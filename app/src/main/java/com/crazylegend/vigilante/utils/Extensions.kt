@file:Suppress("DEPRECATION")

package com.crazylegend.vigilante.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.crazylegend.kotlinextensions.activity.newIntent
import com.crazylegend.kotlinextensions.context.activityManager
import com.crazylegend.vigilante.VigilanteService
import com.crazylegend.vigilante.gps.GPSModel

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

fun Context.startVigilante() {
    val serviceIntent = Intent(this, VigilanteService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(serviceIntent)
    } else {
        startService(serviceIntent)
    }
}

fun Context.stopVigilante(): Boolean {
    val intent = newIntent<VigilanteService>(this)
    return stopService(intent)
}

fun Context.isVigilanteRunning(): Boolean {
    for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
        if (VigilanteService::class.java.name == service.service.className) {
            return true
        }
    }
    return false
}

/**
 * Based on {@link com.android.settingslib.accessibility.AccessibilityUtils#getEnabledServicesFromSettings(Context,int)}
 * @see <a href="https://github.com/android/platform_frameworks_base/blob/d48e0d44f6676de6fd54fd8a017332edd6a9f096/packages/SettingsLib/src/com/android/settingslib/accessibility/AccessibilityUtils.java#L55">AccessibilityUtils</a>
 */
fun Context.hasAccessibilityPermission(): Boolean {
    val expectedComponentName = ComponentName(this, VigilanteService::class.java)
    val enabledServicesSetting = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            ?: return false
    val colonSplitter = TextUtils.SimpleStringSplitter(':')
    colonSplitter.setString(enabledServicesSetting)
    while (colonSplitter.hasNext()) {
        val componentNameString = colonSplitter.next()
        val enabledService = ComponentName.unflattenFromString(componentNameString)
        if (enabledService != null && enabledService == expectedComponentName) return true
    }
    return false
}

fun Context.isGpsEnabled(currentPackageString: String? = null): GPSModel {
    val mode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
    return if (mode != Settings.Secure.LOCATION_MODE_OFF) {
        val locationMode = when (mode) {
            Settings.Secure.LOCATION_MODE_HIGH_ACCURACY -> {
                "High accuracy. Uses GPS, Wi-Fi, and mobile networks to determine location";
            }
            Settings.Secure.LOCATION_MODE_SENSORS_ONLY -> {
                "Device only. Uses GPS to determine location";
            }
            Settings.Secure.LOCATION_MODE_BATTERY_SAVING -> {
                "Battery saving. Uses Wi-Fi and mobile networks to determine location";
            }
            else -> null
        }
        GPSModel(true, locationMode, currentPackageString)
    } else {
        GPSModel(false, currentPackage = currentPackageString)
    }
}