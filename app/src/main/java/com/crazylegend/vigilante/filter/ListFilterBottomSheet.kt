package com.crazylegend.vigilante.filter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractBottomSheet
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import com.crazylegend.vigilante.di.providers.AdapterProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@AndroidEntryPoint
class ListFilterBottomSheet : AbstractBottomSheet<LayoutRecyclerBinding>() {

    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)

    override val viewRes: Int
        get() = R.layout.layout_recycler

    @Inject
    lateinit var adapterProvider: AdapterProvider

    private val adapter by lazy {
        adapterProvider.listFilterAdapter
    }

    private val args by navArgs<ListFilterBottomSheetArgs>()

    companion object {
        const val RESULT_REQUEST_KEY = "reqKeyListFilter"
        const val BUNDLE_ARGUMENT_KEy = "bundleArgKey"
    }

    private val adapterList get() = args.adapterList.toList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter
        if (adapterList.isNullOrEmpty()) {
            dismissAllowingStateLoss()
        }
        adapter.submitList(adapterList)
        adapter.forItemClickListener = forItemClickListener { position, _, _ ->
            setFragmentResult(RESULT_REQUEST_KEY, bundleOf(BUNDLE_ARGUMENT_KEy to position))
            dismissAllowingStateLoss()
        }
    }
}