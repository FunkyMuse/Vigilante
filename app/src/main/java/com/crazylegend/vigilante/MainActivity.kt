package com.crazylegend.vigilante

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.crazylegend.kotlinextensions.misc.requestBatteryOptimizations
import com.crazylegend.kotlinextensions.power.isIgnoringBatteryOptimization
import com.crazylegend.locale.LocaleHelper
import com.crazylegend.view.setOnClickListenerCooldown
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.databinding.ActivityMainBinding
import com.crazylegend.vigilante.utils.DEFAULT_LANGUAGE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        applyOverrideConfiguration(Configuration())
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(overrideConfiguration?.let {
            LocaleHelper.updateConfigurationIfSupported(this, it, DEFAULT_LANGUAGE)
        })
    }

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val navController: NavController
        get() {
            return binding.navHostContainer.findNavController()
        }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private val showBackButtonList
        get() = listOf(
                R.id.settingsFragment,
                R.id.cameraPreferenceFragment,
                R.id.microphonePreferenceFragment,
                R.id.locationPreferenceFragment,
                R.id.crashFragment,
                R.id.appsUsageFragment,
                R.id.screenAccessFragment,
                R.id.listFilterBottomSheet,
                R.id.notificationsFragment,
                R.id.headsetFragment,
                R.id.permissionRequestFragment,
                R.id.permissionDetailsBottomSheet,
                R.id.notificationDetailsFragment,
                R.id.powerFragment,
                R.id.loggingPreferenceFragment,
                R.id.powerDetailsDialog
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isIgnoringBatteryOptimization != true) {
            //show a dialog maybe?
            requestBatteryOptimizations()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.backButton.root.isVisible = destination.id in showBackButtonList
        }
        binding.backButton.root.setOnClickListenerCooldown {
            navController.navigateUp()
        }

    }

}