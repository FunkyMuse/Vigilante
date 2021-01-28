package com.crazylegend.vigilante.crashes

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.fragments.longToast
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.navigation.navigateUpSafe
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.FragmentDetailedCrashBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 1/28/21 to long live and prosper !
 */
@AndroidEntryPoint
class DetailedCrashFragment : AbstractFragment<FragmentDetailedCrashBinding>(R.layout.fragment_detailed_crash) {

    override val binding by viewBinding(FragmentDetailedCrashBinding::bind)

    private val args by navArgs<DetailedCrashFragmentArgs>()
    private val clickedPosition get() = args.position

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            showToast()
        }

        binding.backButton.root.setOnClickListenerCooldown { onResumedUIFunction { findNavController().navigateUpSafe() } }

        val log = CrashyReporter.getLogsAsStrings()?.getOrNull(clickedPosition)
        if (log.isNotNullOrEmpty()) {
            binding.detailedCrash.apply {
                binding.detailedCrash.text = log
            }
        } else {
            findNavController().navigateUpSafe()
            shortToast(R.string.error_occurred)
        }
    }

    private fun showToast() {
        longToast(R.string.too_large_to_copy_to_clipboard)
    }


}