package com.crazylegend.vigilante.home

import android.app.usage.UsageStatsManager
import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.context.usageStatsManager
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.fragments.longToast
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.misc.requestBatteryOptimizations
import com.crazylegend.kotlinextensions.power.isIgnoringBatteryOptimization
import com.crazylegend.kotlinextensions.storage.isDiskEncrypted
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentHomeBinding
import com.crazylegend.vigilante.di.providers.PermissionProvider
import com.crazylegend.vigilante.utils.isVigilanteRunning
import com.crazylegend.vigilante.utils.startVigilante
import com.crazylegend.vigilante.utils.stopVigilante
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
@AndroidEntryPoint
class HomeFragment : AbstractFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var permissionProvider: PermissionProvider

    override val binding by viewBinding(FragmentHomeBinding::bind)

    private val isServiceEnabled get() = permissionProvider.isAccessibilityEnabled

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.controlSwitch.setOnClickListenerCooldown {
            dispatchLogic()
        }

        val ignoring = requireContext().isIgnoringBatteryOptimization ?: false
        if (!ignoring) {
            requireContext().requestBatteryOptimizations()
        }
    }

    private fun dispatchLogic() {
        if (isServiceEnabled) disableTheService() else enableTheService()
    }

    private fun disableTheService() {
        if (permissionProvider.isVigilanteRunning() && permissionProvider.isAccessibilityEnabled) {
            permissionProvider.askForAccessibilityPermissions()
            if (requireContext().isVigilanteRunning()) {
                requireContext().stopVigilante()
            }
            longToast(R.string.disable_the_service)
        }
    }

    private fun enableTheService() {
        if (!permissionProvider.hasAccessibilityPermission()) {
            permissionProvider.askForAccessibilityPermissions()
            longToast(R.string.enable_the_service)
        } else {
            requireContext().startVigilante()
        }
    }

    private val statusImage get() = if (permissionProvider.isAccessibilityEnabled) R.drawable.ic_check else R.drawable.ic_closed

    override fun onResume() {
        super.onResume()
        debug { "IS ENCRYPTED $isDiskEncrypted" }
        binding.controlSwitch.setImageResource(statusImage)
        if (permissionProvider.hasUsageStatsPermission()) {
            val cal: Calendar = Calendar.getInstance()
            cal.add(Calendar.YEAR, -1)
            val queryUsageStats = requireContext().usageStatsManager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    cal.timeInMillis, System.currentTimeMillis())
            queryUsageStats?.asSequence()?.forEach {

            }
        } else {
            permissionProvider.askForUsageStatsPermission()
        }
    }

    private fun Long.toDate(): String = Date(this).toString("dd.MM.yyyy HH:mm:ss")

}


