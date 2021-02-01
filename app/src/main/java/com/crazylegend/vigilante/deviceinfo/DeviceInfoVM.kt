package com.crazylegend.vigilante.deviceinfo

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.defaultDispatcher
import com.crazylegend.kotlinextensions.root.RootUtils
import com.crazylegend.kotlinextensions.storage.isDiskEncrypted
import com.crazylegend.security.MagiskDetector
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractAVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by crazy on 2/1/21 to long live and prosper !
 */
@HiltViewModel
class DeviceInfoVM @Inject constructor(private val magiskDetector: MagiskDetector,
                                       application: Application) : AbstractAVM(application) {

    private val deviceInfoDataList: MutableStateFlow<List<DeviceInfoModel>> = MutableStateFlow(emptyList())
    val deviceInfoList = deviceInfoDataList.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            initList()
        }
    }

    private fun initList() {
        deviceInfoDataList.value = listOf(
                DeviceInfoModel(R.string.disk_status, if (isDiskEncrypted) R.string.disk_encrypted else R.string.disk_not_encrypted),
                DeviceInfoModel(R.string.root_status, if (RootUtils.isDeviceRooted) R.string.device_rooted else R.string.device_not_rooted),
                DeviceInfoModel(R.string.magisk_status, if (magiskDetector.checkForMagisk()) R.string.magisk_detected else R.string.magisk_not_detected),
        )
    }
}