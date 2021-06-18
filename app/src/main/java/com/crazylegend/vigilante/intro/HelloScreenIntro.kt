package com.crazylegend.vigilante.intro

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import com.crazylegend.animations.attentionBounce
import com.crazylegend.animations.playAnimation
import com.crazylegend.view.setOnClickListenerCooldown
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentIntroHelloBinding
import com.crazylegend.vigilante.utils.DEFAULT_ANIM_TIME
import com.crazylegend.vigilante.utils.goToScreen
import com.crazylegend.vigilante.utils.uiAction
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/9/20 to long live and prosper !
 */
@AndroidEntryPoint
class HelloScreenIntro : AbstractFragment<FragmentIntroHelloBinding>(R.layout.fragment_intro_hello) {

    override val binding by viewBinding(FragmentIntroHelloBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnLayout {
            uiAction {
                binding.image.attentionBounce().playAnimation(DEFAULT_ANIM_TIME)
            }
        }
        binding.next.setOnClickListenerCooldown { goToScreen(HelloScreenIntroDirections.destinationExplanation()) }
    }
}