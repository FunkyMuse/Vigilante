package com.crazylegend.vigilante.home

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.fragments.longToast
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.internetdetector.InternetDetector
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

    @Inject
    lateinit var internetDetector: InternetDetector

    override val binding by viewBinding(FragmentHomeBinding::bind)

    private val isServiceEnabled get() = permissionProvider.isAccessibilityEnabled

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        internetDetector.observe(viewLifecycleOwner) {
            val internetText = if (it == true) R.string.online else R.string.offline
            val internetColor = if (it == true) R.color.online else R.color.offline
            binding.networkText.text = getString(internetText)
            binding.networkStatus.setCardBackgroundColor(compatColor(internetColor))
        }

        binding.statusButton.setOnClickListenerCooldown {
            dispatchLogic()
        }

        val ignoring = requireContext().isIgnoringBatteryOptimization ?: false
        if (!ignoring) {
            requireContext().requestBatteryOptimizations()
        }

        binding.crashesButton.setOnClickListenerCooldown {
            shortToast("EYE")
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

    private val buttonInnerCircle get() = if (permissionProvider.isAccessibilityEnabled) R.drawable.ic_inner_ellipse_disable else R.drawable.ic_inner_ellipse_enable
    private val buttonOuterCircle get() = if (permissionProvider.isAccessibilityEnabled) R.drawable.ic_outer_ellipse_disable else R.drawable.ic_outer_ellipse_enable
    private val buttonText get() = if (permissionProvider.isAccessibilityEnabled) R.string.disable_text else R.string.enable_text
    private val diskEncryptionText get() = if (isDiskEncrypted) R.string.disk_encrypted else R.string.disk_not_encrypted

    override fun onPause() {
        super.onPause()
        binding.outerIndicator.animation?.cancel()
    }

    override fun onResume() {
        super.onResume()
        binding.statusText.text = getString(buttonText)
        binding.innerIndicator.setImageResource(buttonInnerCircle)
        binding.outerIndicator.setImageResource(buttonOuterCircle)
        binding.diskText.text = getString(diskEncryptionText)

        /*if (permissionProvider.hasUsageStatsPermission()) {
            val cal: Calendar = Calendar.getInstance()
            cal.add(Calendar.YEAR, -1)
            val queryUsageStats = requireContext().usageStatsManager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    cal.timeInMillis, System.currentTimeMillis())
            queryUsageStats?.asSequence()?.forEach {

            }
        } else {
            permissionProvider.askForUsageStatsPermission()
        }*/
    }

    private fun Long.toDate(): String = Date(this).toString("dd.MM.yyyy HH:mm:ss")

}


