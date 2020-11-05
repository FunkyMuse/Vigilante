package com.crazylegend.vigilante.permissions.requests.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import com.crazylegend.vigilante.abstracts.AbstractAVM
import com.crazylegend.vigilante.permissions.requests.db.PermissionRequestsRepository

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class PermissionRequestsVM @ViewModelInject constructor(
        private val permissionRequestRepository: PermissionRequestsRepository,
        application: Application) : AbstractAVM(application) {

    val permissionRequests = provideDatabaseData { permissionRequestRepository.getAllRequests() }
}