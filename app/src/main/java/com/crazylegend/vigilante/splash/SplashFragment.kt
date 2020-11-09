package com.crazylegend.vigilante.splash

import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import androidx.lifecycle.coroutineScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.crazylegend.coroutines.withMainContext
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.animations.zoomInUp
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentSplashBinding
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
        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenResumed {
            withMainContext {
                binding.logo.zoomInUp().playAnimation(1500).doOnEnd {
                    findNavController().navigate(fragmentDirections)
                }
            }
        }
    }
}