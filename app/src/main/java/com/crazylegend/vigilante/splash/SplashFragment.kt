package com.crazylegend.vigilante.splash

import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import androidx.navigation.NavDirections
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.animations.zoomInUp
import com.crazylegend.kotlinextensions.fragments.finish
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentSplashBinding
import com.crazylegend.vigilante.di.providers.AuthProvider
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.utils.DEFAULT_ANIM_TIME
import com.crazylegend.vigilante.utils.goToScreen
import com.crazylegend.vigilante.utils.uiAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/9/20 to long live and prosper !
 */
@AndroidEntryPoint
class SplashFragment : AbstractFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    @Inject
    lateinit var prefsProvider: DefaultPreferencessProvider

    @Inject
    lateinit var authProvider: AuthProvider

    override val binding by viewBinding(FragmentSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnLayout {
            destinationLogic(if (prefsProvider.isIntroShown) SplashFragmentDirections.destinationHome() else SplashFragmentDirections.destinationIntro())
        }
    }

    private fun destinationLogic(fragmentDirections: NavDirections) {
        uiAction {
            binding.logo.zoomInUp().playAnimation(DEFAULT_ANIM_TIME).doOnEnd {
                checkIfAuthIsEnabled(fragmentDirections)
            }
        }
    }

    private fun checkIfAuthIsEnabled(fragmentDirections: NavDirections) {
        if (prefsProvider.isBiometricAuthEnabled) {
            attemptBiometricAuth(fragmentDirections)
        } else {
            goToScreen(fragmentDirections)
        }
    }

    private fun attemptBiometricAuth(fragmentDirections: NavDirections) {
        authProvider.confirmBiometricAuth(R.string.verification_required, R.string.verify_to_proceed, onAuthFailed = {
            //auth failed action
            authFailed()
        }, onAuthError = { _, _ ->
            //handle auth error message and codes
            authFailed()
        }) {
            //handle successful authentication
            proceedFurther(fragmentDirections)
        }
    }

    private fun proceedFurther(fragmentDirections: NavDirections) {
        goToScreen(fragmentDirections)
    }

    private fun authFailed() {
        uiAction {
            shortToast(R.string.auth_failed)
            finish()
        }
    }
}