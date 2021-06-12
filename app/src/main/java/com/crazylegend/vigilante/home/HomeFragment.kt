package com.crazylegend.vigilante.home

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentHomeBinding
import com.crazylegend.vigilante.di.providers.AdapterProvider
import com.crazylegend.vigilante.di.providers.PermissionProvider
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.home.section.SectionItem
import com.crazylegend.vigilante.utils.DEFAULT_ANIM_TIME
import com.crazylegend.vigilante.utils.goToScreen
import com.crazylegend.vigilante.utils.uiAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
@AndroidEntryPoint
class HomeFragment : AbstractFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var permissionProvider: PermissionProvider

    @Inject
    lateinit var adapterProvider: AdapterProvider

    @Inject
    lateinit var prefsProvider: DefaultPreferencessProvider

    private val sectionAdapter by lazy {
        adapterProvider.sectionAdapter
    }

    private val sectionList
        get() = listOf(
                SectionItem(R.string.permissions_history, R.drawable.security, SectionItem.SectionItemAction.PERMISSIONS),
                SectionItem(R.string.power_history, R.drawable.ic_power_on, SectionItem.SectionItemAction.POWER),
                SectionItem(R.string.headset_history, R.drawable.headphones, SectionItem.SectionItemAction.HEADSET),
                SectionItem(R.string.notifications_history, R.drawable.notification_new, SectionItem.SectionItemAction.NOTIFICATIONS),
                SectionItem(R.string.lock_screen_history, R.drawable.lock, SectionItem.SectionItemAction.LOCK_SCREEN),
                SectionItem(R.string.device_info, R.drawable.ic_info, SectionItem.SectionItemAction.DEVICE_INFO),
        )

    override val binding by viewBinding(FragmentHomeBinding::bind) {
        //dispose any unneeded resources
        themeIcon.animation?.cancel()
    }

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
            val directions = when (item.action) {
                SectionItem.SectionItemAction.DEVICE_INFO -> HomeFragmentDirections.destinationDeviceInfo()
                SectionItem.SectionItemAction.PERMISSIONS -> HomeFragmentDirections.destinationPermissionRequests()
                SectionItem.SectionItemAction.POWER -> HomeFragmentDirections.destinationPower()
                SectionItem.SectionItemAction.HEADSET -> HomeFragmentDirections.destinationHeadset()
                SectionItem.SectionItemAction.NOTIFICATIONS -> HomeFragmentDirections.destinationNotifications()
                SectionItem.SectionItemAction.LOCK_SCREEN -> HomeFragmentDirections.destinationScreenHistory()
            }
            goToScreen(directions)
        }

        binding.statusButton.setOnClickListenerCooldown {
            permissionProvider.dispatchServiceLogic()
        }

        binding.settings.setOnClickListenerCooldown {
            goToScreen(HomeFragmentDirections.destinationSettings())
        }

        binding.themeSwitcher.setOnClickListenerCooldown {
            binding.themeIcon.rotateDarkIcon {
                prefsProvider.changeTheme()
            }
        }

        binding.oneTimePasswordGenerator.setOnClickListenerCooldown {
            findNavController().navigate(HomeFragmentDirections.destinationOtp())
        }

        binding.crashes.setOnClickListenerCooldown {
            if (CrashyReporter.getLogsAsStrings().isNullOrEmpty()) {
                shortToast(R.string.no_crashes)
            } else {
                uiAction { goToScreen(HomeFragmentDirections.destinationCrashes()) }
            }
        }

    }

    private val darkThemeIcon get() = if (prefsProvider.isDarkThemeEnabled) R.drawable.dark_mode else R.drawable.light_mode
    private fun updateDarkThemeIcon() {
        binding.themeIcon.setImageResource(darkThemeIcon)
    }

    private val buttonInnerCircle get() = if (permissionProvider.isVigilanteRunning()) R.drawable.ic_inner_ellipse_disable else R.drawable.ic_inner_ellipse_enable
    private val buttonOuterCircle get() = if (permissionProvider.isVigilanteRunning()) R.drawable.ic_outer_ellipse_disable else R.drawable.ic_outer_ellipse_enable
    private val buttonText get() = if (permissionProvider.isVigilanteRunning()) R.string.disable_text else R.string.enable_text

    private inline fun View.rotateDarkIcon(crossinline endAction: () -> Unit) {
        animate().rotationBy(360f)
                .setStartDelay(0)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction {
                    endAction()
                }
                .setDuration(DEFAULT_ANIM_TIME)
                .start()
    }

    override fun onResume() {
        super.onResume()
        updateDarkThemeIcon()
        binding.statusText.text = getString(buttonText)
        binding.innerIndicator.setImageResource(buttonInnerCircle)
        binding.outerIndicator.setImageResource(buttonOuterCircle)
    }

}


