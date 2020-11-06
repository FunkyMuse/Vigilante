package com.crazylegend.vigilante.permissions.ui.details

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.permissions.db.PermissionRequestsRepository

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class PermissionDetailsVM @ViewModelInject constructor(
        private val permissionRequestRepository: PermissionRequestsRepository,
        @Assisted private val savedStateHandle: SavedStateHandle,
        application: Application) : AbstractAVM(application) {

    private val packageName get() = savedStateHandle.get<String>("packageName")

    val permissionRequests = permissionRequestRepository.getPermissionCountForPackage(packageName.toString())

}