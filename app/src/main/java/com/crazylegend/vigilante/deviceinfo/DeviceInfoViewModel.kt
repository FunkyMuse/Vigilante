package com.crazylegend.vigilante.deviceinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.defaultDispatcher
import com.crazylegend.kotlinextensions.storage.isDiskEncrypted
import com.crazylegend.root.RootUtils
import com.crazylegend.security.MagiskDetector
import com.crazylegend.vigilante.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by crazy on 2/1/21 to long live and prosper !
 */
@HiltViewModel
class DeviceInfoViewModel @Inject constructor(private val magiskDetector: MagiskDetector) : ViewModel() {

    private val deviceInfoDataList: MutableStateFlow<DeviceInfoStatus> =
        MutableStateFlow(DeviceInfoStatus.Loading)
    val deviceInfoList = deviceInfoDataList.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            initList()
        }
    }

    private fun initList() {
        deviceInfoDataList.value = DeviceInfoStatus.Success(
            listOf(
                DeviceInfoModel(
                    R.string.disk_status,
                    if (isDiskEncrypted) R.string.disk_encrypted else R.string.disk_not_encrypted
                ),
                DeviceInfoModel(
                    R.string.root_status,
                    if (RootUtils.isDeviceRooted) R.string.device_rooted else R.string.device_not_rooted
                ),
                DeviceInfoModel(
                    R.string.magisk_status,
                    if (magiskDetector.checkForMagisk()) R.string.magisk_detected else R.string.magisk_not_detected
                ),
            )
        )
    }

    sealed class DeviceInfoStatus {
        object Loading : DeviceInfoStatus()
        data class Success(val list: List<DeviceInfoModel>) : DeviceInfoStatus()
    }
}