@file:Suppress("DEPRECATION")

package com.crazylegend.vigilante.utils

import android.content.Context
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import com.crazylegend.common.tryOrPrint
import com.crazylegend.intent.newIntent
import com.crazylegend.lifecycle.viewCoroutineScope
import com.crazylegend.receivers.isServiceRunning
import com.crazylegend.receivers.startForegroundService
import com.crazylegend.view.dimen
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.database.migrations.CameraAndMicRemovalMigration
import com.crazylegend.vigilante.service.VigilanteService
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

const val DEFAULT_LANGUAGE = "en"
const val VIGILANTE_DB_NAME = "vigilante-db"
const val GITHUB_URL = "https://github.com/FunkyMuse/Vigilante/"
const val MY_OTHER_APPS_URL = "https://funkymuse.dev/apps/"
const val NEW_ISSUE_URL = "${GITHUB_URL}issues/new"
const val DEFAULT_ANIM_TIME = 1000L
val dismissPackages = setOf(
    "com.google.android.permissioncontroller",
    "com.android.systemui",
    "com.google.android.packageinstaller",
    "com.android.packageinstaller",
)

fun Context.startVigilante() {
    startForegroundService<VigilanteService>()
}

fun Context.stopVigilante(): Boolean {
    val intent = newIntent<VigilanteService>()
    return stopService(intent)
}

fun Context.isVigilanteRunning() = isServiceRunning<VigilanteService>()

inline fun <reified T : RoomDatabase> Context.instantiateDatabase(cameraDbName: String): T {
    val passphrase = SQLiteDatabase.getBytes(packageName.toCharArray())
    val factory = SupportFactory(passphrase)
    return Room.databaseBuilder(this, T::class.java, cameraDbName)
        .addMigrations(CameraAndMicRemovalMigration())
        .openHelperFactory(factory)
        .build()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.assistedViewModel(
    crossinline viewModelProducer: (SavedStateHandle) -> T
) = viewModels<T> {
    object : AbstractSavedStateViewModelFactory(this, arguments) {
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
            viewModelProducer(handle) as T
    }
}

fun Fragment.goToScreen(directions: NavDirections) {
    uiAction { tryOrPrint { findNavController().navigate(directions) } }
}

inline fun Fragment.uiAction(crossinline action: () -> Unit) {
    viewCoroutineScope.launchWhenResumed { action() }
}

fun PreferenceFragmentCompat.addSpacingForPreferenceBackButton() {
    listView.apply {
        clipToPadding = false
        updatePadding(bottom = dimen(R.dimen.padding_bottom_scroll).toInt())
    }
}