package com.crazylegend.vigilante.splash

import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.animations.zoomInUp
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentSplashBinding
import com.crazylegend.vigilante.utils.DEFAULT_ANIM_TIME
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/9/20 to long live and prosper !
 */
@AndroidEntryPoint
class SplashFragment : AbstractFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override val binding by viewBinding(FragmentSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnLayout {
            goToDestination(if (prefsProvider.isIntroShown) SplashFragmentDirections.destinationHome() else SplashFragmentDirections.destinationIntro())
        }
    }

    private fun goToDestination(fragmentDirections: NavDirections) {
        onResumedUIFunction {
            binding.logo.zoomInUp().playAnimation(DEFAULT_ANIM_TIME).doOnEnd {
                findNavController().navigate(fragmentDirections)
            }
        }
    }
}