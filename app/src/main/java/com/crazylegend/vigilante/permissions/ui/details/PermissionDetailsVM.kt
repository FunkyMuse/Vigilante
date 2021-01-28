package com.crazylegend.vigilante.permissions.ui.details

import android.app.Application
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.permissions.db.PermissionRequestsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class PermissionDetailsVM @AssistedInject constructor(
        permissionRequestRepository: PermissionRequestsRepository,
        @Assisted private val packageName: String,
        application: Application) : AbstractAVM(application) {

    @AssistedFactory
    interface PermissionDetailsVMFactory {
        fun create(packageName: String): PermissionDetailsVM
    }

    val permissionRequests = permissionRequestRepository.getPermissionRequestsForPackage(packageName)

}