package com.crazylegend.vigilante.crashes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.kotlinextensions.context.copyToClipboard
import com.crazylegend.kotlinextensions.fragments.shortToast

import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.toaster.Toaster
import com.crazylegend.kotlinextensions.tryOrElse
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import com.crazylegend.vigilante.di.providers.AdapterProvider
import com.crazylegend.vigilante.utils.NEW_ISSUE_URL
import com.crazylegend.vigilante.utils.goToScreen
import com.crazylegend.vigilante.utils.uiAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@AndroidEntryPoint
class CrashFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler) {

    override val binding by viewBinding(LayoutRecyclerBinding::bind)
    private val crashVM by viewModels<CrashViewModel>()

    @Inject
    lateinit var toaster: Toaster

    @Inject
    lateinit var adapterProvider: AdapterProvider

    private val crashesAdapter by lazy {
        adapterProvider.crashesAdapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = crashesAdapter
        crashesAdapter.submitList(crashVM.crashes)
        crashesAdapter.forItemClickListener = forItemClickListener { position, item, _ ->
            tryOrElse(defaultBlock = {
                onUnableToCopyCrash(position)
            }) {
                uiAction { shareCrash(item) }
            }
        }
    }

    private fun onUnableToCopyCrash(position: Int) {
        goToScreen(CrashFragmentDirections.actionDetailedCrash(position))
    }

    private fun shareCrash(item: String) {
        requireContext().copyToClipboard(item)
        toaster.shortToast(R.string.crash_copied_to_clipboard)
        requireContext().openWebPage(NEW_ISSUE_URL) {
            shortToast(R.string.web_browser_required)
        }
    }
}