package com.crazylegend.vigilante.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.fragments.longToast
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.internetdetector.InternetDetector
import com.crazylegend.kotlinextensions.misc.requestBatteryOptimizations
import com.crazylegend.kotlinextensions.power.isIgnoringBatteryOptimization
import com.crazylegend.kotlinextensions.storage.isDiskEncrypted
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.navigation.navigateSafe
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentHomeBinding
import com.crazylegend.vigilante.di.providers.PermissionProvider
import com.crazylegend.vigilante.home.section.SectionItem
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

    private val sectionAdapter by lazy {
        adapterProvider.sectionAdapter
    }

    private val sectionList
        get() = listOf(
                SectionItem(R.string.camera_history, R.drawable.camera),
                SectionItem(R.string.microphone_history, R.drawable.microphone),
                SectionItem(R.string.permissions_history, R.drawable.security),
                SectionItem(R.string.clipboard_history, R.drawable.clipboard),
                SectionItem(R.string.headset_history, R.drawable.headphones),
                SectionItem(R.string.notifications_history, R.drawable.notification_new),
                SectionItem(R.string.lock_screen_history, R.drawable.lock),
                SectionItem(R.string.apps_usage_history, R.drawable.data),
        )

    override val binding by viewBinding(FragmentHomeBinding::bind)

    private val isServiceEnabled get() = permissionProvider.isAccessibilityEnabled

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sections.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(false)
            adapter = sectionAdapter
        }
        sectionAdapter.submitList(sectionList)


        internetDetector.observe(viewLifecycleOwner) {
            updateInternetStatusUI(it)
        }

        binding.statusButton.setOnClickListenerCooldown {
            dispatchLogic()
        }

        val ignoring = requireContext().isIgnoringBatteryOptimization ?: false
        if (!ignoring) {
            requireContext().requestBatteryOptimizations()
        }

        binding.crashes.setOnClickListenerCooldown {
            shortToast("EYE")
        }

        binding.settings.setOnClickListenerCooldown {
            findNavController().navigateSafe(HomeFragmentDirections.destinationSettings())
        }

        binding.themeSwitcher.setOnClickListenerCooldown {
            prefsProvider.changeTheme()
            updateDarkThemeIcon()
        }

        binding.crashes.setOnClickListenerCooldown {
            if (CrashyReporter.getLogsAsStrings().isNullOrEmpty()) {
                shortToast(R.string.no_crashes)
            } else {
                findNavController().navigateSafe(HomeFragmentDirections.destinationCrashes())
            }
        }
    }

    private fun updateInternetStatusUI(trigger: Boolean?) {
        val internetText = if (trigger == true) R.string.online else R.string.offline
        val internetColor = if (trigger == true) R.color.online else R.color.offline
        binding.networkText.text = getString(internetText)
        binding.networkStatus.setCardBackgroundColor(compatColor(internetColor))
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

    private val darkThemeIcon get() = if (prefsProvider.isDarkThemeEnabled) R.drawable.dark_mode else R.drawable.light_mode
    private fun updateDarkThemeIcon() {
        binding.themeIcon.setImageResource(darkThemeIcon)
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
        updateDarkThemeIcon()
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


