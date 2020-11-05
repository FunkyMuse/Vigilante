package com.crazylegend.vigilante.headset

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.headset.database.HeadsetRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created by crazy on 10/30/20 to long live and prosper !
 */
@AndroidEntryPoint
class HeadsetReceiver : BroadcastReceiver() {

    @Inject
    lateinit var headsetRepository: HeadsetRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            val connectionType = intent.getIntExtra("state", 0)
            val headsetModel = HeadsetModel(Date(currentTimeMillis), connectionType)
            GlobalScope.launch(ioDispatcher) {
                withContext(NonCancellable) {
                    headsetRepository.insertHeadsetRecord(headsetModel)
                }
            }
        }
    }
}