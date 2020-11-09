package com.crazylegend.vigilante.deviceinfo

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.root.RootUtils
import com.crazylegend.kotlinextensions.storage.isDiskEncrypted
import com.crazylegend.security.MagiskDetector
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@AndroidEntryPoint
class DeviceInfoFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler) {

    @Inject
    lateinit var magiskDetector: MagiskDetector

    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)

    private val adapter by lazy {
        adapterProvider.deviceInfoAdapter
    }

    private val deviceInfoList
        get() = listOf(
                DeviceInfoModel(R.string.disk_status, if (isDiskEncrypted) R.string.disk_encrypted else R.string.disk_not_encrypted),
                DeviceInfoModel(R.string.root_status, if (RootUtils.isDeviceRooted) R.string.device_rooted else R.string.device_not_rooted),
                DeviceInfoModel(R.string.magisk_status, if (magiskDetector.checkForMagisk()) R.string.magisk_detected else R.string.magisk_not_detected),
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        adapter.submitList(deviceInfoList)
    }
}