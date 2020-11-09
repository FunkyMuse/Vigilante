package com.crazylegend.vigilante.splash

import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.crazylegend.coroutines.withMainContext
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.animations.zoomInUp
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentSplashBinding

/**
 * Created by crazy on 11/9/20 to long live and prosper !
 */
class SplashFragment : AbstractFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override val binding by viewBinding(FragmentSplashBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnLayout {
            goHome()
        }
    }

    private fun goHome() {
        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenResumed {
            withMainContext {
                binding.logo.zoomInUp().playAnimation(1500).doOnEnd {
                    findNavController().navigate(SplashFragmentDirections.destinationHome())
                }
            }
        }
    }
}