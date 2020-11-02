package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.crazylegend.kotlinextensions.accessibility.hasAccessibilityPermission
import com.crazylegend.kotlinextensions.accessibility.isAccessibilityServiceRunning
import com.crazylegend.kotlinextensions.context.accessibilityManager
import com.crazylegend.kotlinextensions.context.longToast
import com.crazylegend.kotlinextensions.permissions.hasUsageStatsPermission
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.VigilanteService
import com.crazylegend.vigilante.di.qualifiers.FragmentContext
import com.crazylegend.vigilante.utils.startVigilante
import com.crazylegend.vigilante.utils.stopVigilante
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@FragmentScoped
class PermissionProvider @Inject constructor(
        @FragmentContext private val context: Context) {

    val isAccessibilityEnabled get() = context.accessibilityManager?.isEnabled ?: false

    private fun isVigilanteRunning() = context.isAccessibilityServiceRunning<VigilanteService>()

    private fun askForAccessibilityPermissions() = context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

    private fun hasAccessibilityPermission() = context.hasAccessibilityPermission<VigilanteService>()

    fun askForUsageStatsPermission() = context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

    fun hasUsageStatsPermission(): Boolean = context.hasUsageStatsPermission()

    fun dispatchServiceLogic() {
        if (isAccessibilityEnabled) disableTheService() else enableTheService()
    }

    private fun disableTheService() {
        if (isVigilanteRunning() && isAccessibilityEnabled) {
            askForAccessibilityPermissions()
            if (isVigilanteRunning()) {
                context.longToast(R.string.disable_the_service)
                context.stopVigilante()
            }
        }
    }

    private fun enableTheService() {
        if (!hasAccessibilityPermission()) {
            context.longToast(R.string.enable_the_service)
            askForAccessibilityPermissions()
        } else {
            context.startVigilante()
        }
    }
}