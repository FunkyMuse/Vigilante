package com.crazylegend.vigilante.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.crazylegend.kotlinextensions.misc.requestBatteryOptimizations
import com.crazylegend.kotlinextensions.power.isIgnoringBatteryOptimization
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.navigation.navigateUpSafe
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val navController: NavController
        get() {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
            return navHostFragment.navController
        }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private val showBackButtonList
        get() = listOf(
                R.id.settingsFragment, R.id.crashFragment, R.id.cameraAccessFragment,
                R.id.microphoneAccessFragment, R.id.appsUsageFragment, R.id.screenAccessFragment,
                R.id.listFilterBottomSheet, R.id.notificationsFragment, R.id.headsetFragment,
                R.id.permissionRequestFragment, R.id.permissionDetailsBottomSheet, R.id.notificationDetailsFragment,
                R.id.powerFragment, R.id.powerDetailsDialog
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
            binding.backButton.isVisible = destination.id in showBackButtonList
        }
        binding.backButton.setOnClickListenerCooldown {
            navController.navigateUpSafe()
        }
    }
}