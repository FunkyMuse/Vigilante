package com.crazylegend.vigilante.intro

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentIntroHelloBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/9/20 to long live and prosper !
 */
@AndroidEntryPoint
class HelloScreenIntro : AbstractFragment<FragmentIntroHelloBinding>(R.layout.fragment_intro_hello) {

    override val binding by viewBinding(FragmentIntroHelloBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.next.setOnClickListenerCooldown { goToScreen(HelloScreenIntroDirections.destinationExplanation()) }
    }
}