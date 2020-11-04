package com.crazylegend.vigilante.appsUsage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@AndroidEntryPoint
class AppsUsageFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler) {

    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)

    private val appsUsageVM by viewModels<AppsUsageVM>()
    private val adapter by lazy {
        adapterProvider.appsUsageAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        appsUsageVM.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

    }
}