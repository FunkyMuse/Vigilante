package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.crazylegend.kotlinextensions.accessibility.hasAccessibilityPermission
import com.crazylegend.kotlinextensions.accessibility.isAccessibilityServiceRunning
import com.crazylegend.kotlinextensions.context.accessibilityManager
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

}