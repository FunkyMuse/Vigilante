package com.crazylegend.vigilante.di.providers

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
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
        @FragmentContext private val context: Context,
        private val contentResolver: ContentResolver) {

    val isAccessibilityEnabled get() = context.accessibilityManager?.isEnabled ?: false

    fun isVigilanteRunning(): Boolean {
        val settingsString = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        return settingsString != null && settingsString.contains("${context.packageName}/${VigilanteService::class.java.name}")
    }

    fun askForAccessibilityPermissions() = context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))

    /**
     * Based on {@link com.android.settingslib.accessibility.AccessibilityUtils#getEnabledServicesFromSettings(Context,int)}
     * @see <a href="https://github.com/android/platform_frameworks_base/blob/d48e0d44f6676de6fd54fd8a017332edd6a9f096/packages/SettingsLib/src/com/android/settingslib/accessibility/AccessibilityUtils.java#L55">AccessibilityUtils</a>
     */
    fun hasAccessibilityPermission(): Boolean {
        val expectedComponentName = ComponentName(context, VigilanteService::class.java)
        val enabledServicesSetting = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
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


}