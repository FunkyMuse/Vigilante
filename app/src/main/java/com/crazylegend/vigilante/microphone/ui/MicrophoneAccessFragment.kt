package com.crazylegend.vigilante.microphone.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@AndroidEntryPoint
class MicrophoneAccessFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler), LoadingDBsInFragments {

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider
    private val microphoneAccessVM by viewModels<MicrophoneAccessVM>()
    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)

    @Inject
    lateinit var micAccessAdapter: MicrophoneAccessAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseLoadingProvider.provideListState(microphoneAccessVM.microphoneAccess, binding.recycler, binding.noDataViewHolder.noDataView,
                micAccessAdapter)
    }
}