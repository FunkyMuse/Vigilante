package com.crazylegend.vigilante.permissions.ui.details

import androidx.lifecycle.ViewModel
import com.crazylegend.vigilante.permissions.db.PermissionRequestsDAO
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class PermissionDetailsViewModel @AssistedInject constructor(
        permissionRequestRepository: PermissionRequestsDAO,
        @Assisted private val packageName: String) : ViewModel() {

    @AssistedFactory
    interface PermissionDetailsVMFactory {
        fun create(packageName: String): PermissionDetailsViewModel
    }

    val permissionRequests = permissionRequestRepository.getPermissionRequestsForPackage(packageName)

}