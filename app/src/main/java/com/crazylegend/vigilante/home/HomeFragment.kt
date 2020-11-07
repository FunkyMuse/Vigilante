package com.crazylegend.vigilante.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.dateAndTime.toString
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.storage.isDiskEncrypted
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.navigation.navigateSafe
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentHomeBinding
import com.crazylegend.vigilante.di.providers.PermissionProvider
import com.crazylegend.vigilante.home.section.SectionItem
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

    private val sectionAdapter by lazy {
        adapterProvider.sectionAdapter
    }

    private val sectionList
        get() = listOf(
                SectionItem(R.string.camera_history, R.drawable.camera, SectionItem.SectionItemAction.CAMERA),
                SectionItem(R.string.microphone_history, R.drawable.microphone, SectionItem.SectionItemAction.MIC),
                SectionItem(R.string.permissions_history, R.drawable.security, SectionItem.SectionItemAction.PERMISSIONS),
                SectionItem(R.string.power_history, R.drawable.ic_power_on, SectionItem.SectionItemAction.POWER),
                SectionItem(R.string.headset_history, R.drawable.headphones, SectionItem.SectionItemAction.HEADSET),
                SectionItem(R.string.notifications_history, R.drawable.notification_new, SectionItem.SectionItemAction.NOTIFICATIONS),
                SectionItem(R.string.lock_screen_history, R.drawable.lock, SectionItem.SectionItemAction.LOCK_SCREEN),
                SectionItem(R.string.apps_usage_history, R.drawable.data, SectionItem.SectionItemAction.APPS_USAGE),
        )

    override val binding by viewBinding(FragmentHomeBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sections.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(false)
            adapter = sectionAdapter
            isNestedScrollingEnabled = false
        }
        sectionAdapter.submitList(sectionList)

        sectionAdapter.forItemClickListener = forItemClickListener { _, item, _ ->
            if (item.action == SectionItem.SectionItemAction.APPS_USAGE) {
                permissionProvider.propagateAppsUsageClick {
                    findNavController().navigateSafe(HomeFragmentDirections.destinationAppsUsage())
                }
            } else {
                val directions = when (item.action) {
                    SectionItem.SectionItemAction.CAMERA -> HomeFragmentDirections.destinationCameraHistory()
                    SectionItem.SectionItemAction.MIC -> HomeFragmentDirections.destinationMicrophoneHistory()
                    SectionItem.SectionItemAction.PERMISSIONS -> HomeFragmentDirections.destinationPermissionRequests()
                    SectionItem.SectionItemAction.POWER -> HomeFragmentDirections.destinationPower()
                    SectionItem.SectionItemAction.HEADSET -> HomeFragmentDirections.destinationHeadset()
                    SectionItem.SectionItemAction.NOTIFICATIONS -> HomeFragmentDirections.destinationNotifications()
                    SectionItem.SectionItemAction.LOCK_SCREEN -> HomeFragmentDirections.destinationScreenHistory()
                    else -> null
                }
                directions?.let { findNavController().navigateSafe(it) }
            }
        }

        binding.statusButton.setOnClickListenerCooldown {
            permissionProvider.dispatchServiceLogic()
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
    }

    private fun Long.toDate(): String = Date(this).toString("dd.MM.yyyy HH:mm:ss")

}


