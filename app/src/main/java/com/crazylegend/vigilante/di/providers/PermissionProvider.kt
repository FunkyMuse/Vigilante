package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.os.bundleOf
import com.crazylegend.kotlinextensions.accessibility.hasAccessibilityPermission
import com.crazylegend.kotlinextensions.accessibility.isAccessibilityServiceRunning
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.qualifiers.FragmentContext
import com.crazylegend.vigilante.service.VigilanteService
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


    fun isVigilanteRunning() = context.isAccessibilityServiceRunning<VigilanteService>()

    private fun askForAccessibilityPermissions() = context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        val argKey = ":settings:fragment_args_key"
        val showFragsKey = ":settings:show_fragment_args"
        val showArgs = context.packageName + "/" + VigilanteService::class.java.name
        putExtra(argKey, showArgs)
        putExtra(showFragsKey, bundleOf(argKey to showArgs))
    })

    private fun hasAccessibilityPermission() = context.hasAccessibilityPermission<VigilanteService>()

    fun dispatchServiceLogic() {
        if (isVigilanteRunning()) disableTheService() else enableTheService()
    }

    private fun disableTheService() {
        if (isVigilanteRunning()) {
            context.shortToast(R.string.disable_the_service)
            context.stopVigilante()
            askForAccessibilityPermissions()
        }
    }

    private fun enableTheService() {
        if (!hasAccessibilityPermission()) {
            context.shortToast(R.string.enable_the_service)
            askForAccessibilityPermissions()
        } else {
            context.startVigilante()
        }
    }
}