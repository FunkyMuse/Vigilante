package com.crazylegend.vigilante.permissions.ui.request

import android.app.Application
import com.crazylegend.vigilante.abstracts.AbstractPagingViewModel
import com.crazylegend.vigilante.permissions.db.PermissionRequestsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@HiltViewModel
class PermissionRequestsViewModel @Inject constructor(
        private val permissionRequestRepository: PermissionRequestsRepository,
        application: Application) : AbstractPagingViewModel(application) {

    val permissionRequests = provideDatabaseData { permissionRequestRepository.getAllPermissionRequests() }
    val totalPermissionRequests = permissionRequestRepository.permissionRequestsCount()
}