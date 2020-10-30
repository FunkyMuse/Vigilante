package com.crazylegend.vigilante.di.providers

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Process
import android.provider.Settings
import com.crazylegend.kotlinextensions.accessibility.hasAccessibilityPermission
import com.crazylegend.kotlinextensions.accessibility.isAccessibilityServiceRunning
import com.crazylegend.kotlinextensions.context.accessibilityManager
import com.crazylegend.kotlinextensions.context.appOpsManager
import com.crazylegend.vigilante.VigilanteService
import com.crazylegend.vigilante.di.qualifiers.FragmentContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@FragmentScoped
class PermissionProvider @Inject constructor(
        @FragmentContext private val context: Context) {

    val isAccessibilityEnabled get() = context.accessibilityManager?.isEnabled ?: false

    fun isVigilanteRunning() = context.isAccessibilityServiceRunning<VigilanteService>()

    fun askForAccessibilityPermissions() = context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

    fun hasAccessibilityPermission() = context.hasAccessibilityPermission<VigilanteService>()

    fun askForUsageStatsPermission() = context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))

    fun hasUsageStatsPermission(): Boolean {
        val appOps = context.appOpsManager ?: return false
        val mode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
        } else {
            appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
        }
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }
}