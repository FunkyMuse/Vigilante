package com.crazylegend.vigilante.crashes

import android.os.Bundle
import android.view.View
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.kotlinextensions.context.copyToClipboard
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.EdgeToEdgeScrolling
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import com.crazylegend.vigilante.utils.EdgeToEdge
import com.crazylegend.vigilante.utils.NEW_ISSUE_URL
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@AndroidEntryPoint
class CrashFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler), EdgeToEdgeScrolling {

    override val binding by viewBinding(LayoutRecyclerBinding::bind)

    private val crashesAdapter by lazy {
        adapterProvider.crashesAdapter
    }

    override fun edgeToEdgeScrollingContent() {
        EdgeToEdge.setUpScrollingContent(binding.recycler)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = crashesAdapter
        crashesAdapter.submitList(CrashyReporter.getLogsAsStrings())
        crashesAdapter.forItemClickListener = forItemClickListener { _, item, _ ->
            requireContext().copyToClipboard(item)
            shortToast(R.string.crash_copied_to_clipboard)
            requireContext().openWebPage(NEW_ISSUE_URL) {
                shortToast(R.string.web_browser_required)
            }
        }
    }
}