package com.crazylegend.vigilante.deviceinfo

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.crazylegend.lifecycle.repeatingJobOnStarted
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@AndroidEntryPoint
class DeviceInfoFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler) {

    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)

    @Inject
    lateinit var adapter: DeviceInfoAdapter

    private val deviceInfoVM by viewModels<DeviceInfoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        repeatingJobOnStarted {
            deviceInfoVM.deviceInfoList.collect {
                binding.progressIndicator.isVisible =
                        it is DeviceInfoViewModel.DeviceInfoStatus.Loading
                if (it is DeviceInfoViewModel.DeviceInfoStatus.Success) {
                    adapter.submitList(it.list)
                }
            }
        }
    }
}