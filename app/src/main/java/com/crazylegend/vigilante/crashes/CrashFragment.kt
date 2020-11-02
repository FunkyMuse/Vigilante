package com.crazylegend.vigilante.crashes

import android.os.Bundle
import android.view.View
import com.crazylegend.crashyreporter.CrashyReporter
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
@AndroidEntryPoint
class CrashFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler) {

    override val binding by viewBinding(LayoutRecyclerBinding::bind)

    private val crashesAdapter by lazy {
        adapterProvider.crashesAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = crashesAdapter
        crashesAdapter.submitList(CrashyReporter.getLogsAsStrings())
    }
}