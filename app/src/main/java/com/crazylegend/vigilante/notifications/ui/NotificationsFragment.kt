package com.crazylegend.vigilante.notifications.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.navigation.navigateSafe
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.contracts.LoadingDBsInFragments
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import com.crazylegend.vigilante.di.providers.DatabaseLoadingProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@AndroidEntryPoint
class NotificationsFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler), LoadingDBsInFragments {

    @Inject
    override lateinit var databaseLoadingProvider: DatabaseLoadingProvider

    override val binding by viewBinding(LayoutRecyclerBinding::bind)

    @Inject
    lateinit var adapter: NotificationsAdapter

    private val notificationsVM by viewModels<NotificationsVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseLoadingProvider.provideListState(notificationsVM.notificationsAccess, binding.recycler, binding.noDataViewHolder.noDataView, adapter)
        adapter.forItemClickListener = forItemClickListener { _, item, _ ->
            findNavController().navigateSafe(NotificationsFragmentDirections.destinationNotificationDetails(item.id))
        }
    }

}