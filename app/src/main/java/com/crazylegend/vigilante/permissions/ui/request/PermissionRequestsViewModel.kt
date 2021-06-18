package com.crazylegend.vigilante.permissions.ui.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.vigilante.paging.PagingProvider
import com.crazylegend.vigilante.permissions.db.PermissionRequestsDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@HiltViewModel
class PermissionRequestsViewModel @Inject constructor(
        private val permissionRequestRepository: PermissionRequestsDAO,
        pagingProvider: PagingProvider
) : ViewModel() {

    val permissionRequests =
        pagingProvider.provideDatabaseData(viewModelScope) { permissionRequestRepository.getAllPermissionRequests() }
    val totalPermissionRequests = permissionRequestRepository.permissionRequestsCount()
}