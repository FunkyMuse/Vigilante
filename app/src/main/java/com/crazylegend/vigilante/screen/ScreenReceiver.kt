package com.crazylegend.vigilante.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.vigilante.di.modules.coroutines.appScope.ApplicationScope
import com.crazylegend.vigilante.screen.db.ScreenModel
import com.crazylegend.vigilante.screen.db.ScreenRepository
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */
@ServiceScoped
class ScreenReceiver @Inject constructor(
        private val screenRepository: ScreenRepository,
        @ApplicationScope private val appScope: CoroutineScope
) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        val screenModel = when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> ScreenModel(wasScreenLocked = true)
            Intent.ACTION_SCREEN_ON -> ScreenModel(wasScreenLocked = false)
            else -> null
        }
        screenModel?.apply {
            appScope.launch() {
                withContext(NonCancellable) {
                    screenRepository.insertScreenAction(this@apply)
                }
            }
        }
    }
}