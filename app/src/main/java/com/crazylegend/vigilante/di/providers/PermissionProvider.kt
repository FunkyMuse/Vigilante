package com.crazylegend.vigilante.di.providers

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.crazylegend.kotlinextensions.context.accessibilityManager
import com.crazylegend.vigilante.VigilanteService
import com.crazylegend.vigilante.di.qualifiers.FragmentContext
import com.crazylegend.vigilante.utils.hasAccessibilityPermission
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@FragmentScoped
class PermissionProvider @Inject constructor(
        @FragmentContext private val context: Context,
        private val contentResolver: ContentResolver) {

    val isAccessibilityEnabled get() = context.accessibilityManager?.isEnabled ?: false

    fun isVigilanteRunning(): Boolean {
        val settingsString = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return settingsString != null && settingsString.contains("${context.packageName}/${VigilanteService::class.java.name}")
    }

    fun askForAccessibilityPermissions() = context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

    fun hasAccessibilityPermission(): Boolean = context.hasAccessibilityPermission()

}