package com.crazylegend.vigilante.headset.ui

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.headset.database.HeadsetRepository

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class HeadsetVM @ViewModelInject constructor(
        private val repo: HeadsetRepository,
        @Assisted private val savedStateHandle: SavedStateHandle,
        application: Application) : AbstractAVM(application) {

    val headsetData = provideDatabaseData { repo.getAllHeadsetRecords() }

}