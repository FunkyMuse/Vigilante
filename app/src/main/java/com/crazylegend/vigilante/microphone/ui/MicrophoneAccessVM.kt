package com.crazylegend.vigilante.microphone.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.database.DBResult
import com.crazylegend.database.coroutines.makeDBCallListFlow
import com.crazylegend.vigilante.microphone.db.MicrophoneModel
import com.crazylegend.vigilante.microphone.db.MicrophoneRepository

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
class MicrophoneAccessVM @ViewModelInject constructor(
        private val microphoneRepository: MicrophoneRepository,
        application: Application) : AndroidViewModel(application) {

    private val microphoneDataList: MutableLiveData<DBResult<List<MicrophoneModel>>> = MutableLiveData()
    val microphoneAccess: LiveData<DBResult<List<MicrophoneModel>>> = microphoneDataList

    init {
        getAll()
    }

    private fun getAll() {
        makeDBCallListFlow(microphoneDataList) {
            microphoneRepository.getAllMicrophoneRecords()
        }
    }
}