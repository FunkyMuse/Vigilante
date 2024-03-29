package com.crazylegend.vigilante.crashes.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.string.isNotNullOrEmpty

import com.crazylegend.toaster.Toaster
import com.crazylegend.view.setOnClickListenerCooldown


import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentDetailedCrashBinding
import com.crazylegend.vigilante.utils.assistedViewModel
import com.crazylegend.vigilante.utils.uiAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 1/28/21 to long live and prosper !
 */
@AndroidEntryPoint
class DetailedCrashFragment : AbstractFragment<FragmentDetailedCrashBinding>(R.layout.fragment_detailed_crash) {

    override val binding by viewBinding(FragmentDetailedCrashBinding::bind)

    private val args by navArgs<DetailedCrashFragmentArgs>()
    private val clickedPosition get() = args.position

    @Inject
    lateinit var toaster: Toaster

    @Inject
    lateinit var detailedCrashVMFactory: DetailedCrashViewModel.DetailedCrashVMFactory

    private val detailedCrashVM by assistedViewModel {
        detailedCrashVMFactory.create(clickedPosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            showToast()
        }

        binding.backButton.root.setOnClickListenerCooldown { uiAction { findNavController().navigateUp() } }

        detailedCrashVM.detailedCrash.apply {
            if (this.isNotNullOrEmpty()) {
                binding.detailedCrash.text = this
            } else {
                findNavController().navigateUp()
                toaster.shortToast(R.string.error_occurred)
            }
        }

    }

    private fun showToast() {
        toaster.longToast(R.string.too_large_to_copy_to_clipboard)
    }


}