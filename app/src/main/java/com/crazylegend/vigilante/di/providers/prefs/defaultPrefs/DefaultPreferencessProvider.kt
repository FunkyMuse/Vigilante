package com.crazylegend.vigilante.di.providers.prefs.defaultPrefs

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.crazylegend.kotlinextensions.misc.disableNightMode
import com.crazylegend.kotlinextensions.misc.enableNightMode
import com.crazylegend.sharedpreferences.putBoolean
import com.crazylegend.sharedpreferences.putString
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import com.crazylegend.vigilante.historyDeletionWorker.HistoryDeletionWorker
import com.crazylegend.vigilante.settings.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@Singleton
class DefaultPreferencessProvider @Inject constructor(
        @EncryptedPrefs private val defaultPrefs: SharedPreferences,
        private val workManager: WorkManager
) : DefaultPreferences {

    //region date format
    override fun updateDateFormat(value: String) = defaultPrefs.putString(DATE_PREF_KEY, value)
    override val getDateFormat
        get() = defaultPrefs.getString(DATE_PREF_KEY, DEFAULT_DATE_FORMAT) ?: DEFAULT_DATE_FORMAT
    //endregion

    //region theme
    override val isDarkThemeEnabled get() = defaultPrefs.getBoolean(THEME_PREF_KEY, false)
    override fun changeTheme() {
        defaultPrefs.edit(true) { putBoolean(THEME_PREF_KEY, !isDarkThemeEnabled) }
        applyThemeLogic()
    }

    fun applyThemeLogic() {
        if (isDarkThemeEnabled) enableNightMode() else disableNightMode()
    }
    //endregion


    //region notification exclusion
    override val isVigilanteExcludedFromNotifications
        get() = defaultPrefs.getBoolean(
                EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY,
                false
        )

    override fun setExcludeVigilanteFromNotificationsStatus(newValue: Boolean) =
            defaultPrefs.putBoolean(EXCLUDE_VIGILANTE_FROM_NOTIFICATIONS_PREF_KEY, newValue)
    //endregion


    //region intro
    override val isIntroShown get() = defaultPrefs.getBoolean(INTRO_PREF, false)
    override fun setIntroShown() {
        defaultPrefs.putBoolean(INTRO_PREF, true)
    }
    //endregion

    //region biometric auth
    override val isBiometricAuthEnabled
        get() = defaultPrefs.getBoolean(
                BIOMETRIC_AUTH_PREF_KEY,
                false
        )

    override fun updateBiometricStatus(status: Boolean) =
            defaultPrefs.putBoolean(BIOMETRIC_AUTH_PREF_KEY, status)
    //endregion


    //region history deletion
    override fun scheduleDeletionHistory() {
        val builder = PeriodicWorkRequestBuilder<HistoryDeletionWorker>(30, TimeUnit.DAYS)
        builder.addTag(HistoryDeletionWorker.WORK_ID)
        workManager.enqueueUniquePeriodicWork(
                HistoryDeletionWorker.WORK_ID,
                ExistingPeriodicWorkPolicy.KEEP,
                builder.build()
        )
    }

    override fun cancelDeletionHistory() {
        workManager.cancelAllWorkByTag(HistoryDeletionWorker.WORK_ID)
    }
    //endregion
}