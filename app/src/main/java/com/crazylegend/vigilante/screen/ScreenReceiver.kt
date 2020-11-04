package com.crazylegend.vigilante.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.vigilante.screen.db.ScreenModel
import com.crazylegend.vigilante.screen.db.ScreenRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */
@AndroidEntryPoint
class ScreenReceiver : BroadcastReceiver() {

    @Inject
    lateinit var screenRepository: ScreenRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return

        if (intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
            GlobalScope.launch(ioDispatcher) {
                withContext(NonCancellable) {
                    screenRepository.insertScreenModel(ScreenModel(wasScreenLocked = true))
                }
            }
        } else if (intent.action.equals(Intent.ACTION_SCREEN_ON)) {
            GlobalScope.launch(ioDispatcher) {
                withContext(NonCancellable) {
                    screenRepository.insertScreenModel(ScreenModel(wasScreenLocked = false))
                }
            }
        }
    }
}