package com.crazylegend.vigilante.permissions.requests.db

import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class PermissionRequestsRepository @Inject constructor(private val permissionRequestDAO: PermissionRequestsDAO) {

    fun getAllRequests() = permissionRequestDAO.getAllPermissionRequests()

    suspend fun insertPermissionRequest(permissionRequestModel: PermissionRequestModel) = permissionRequestDAO.insertPermissionRequest(permissionRequestModel)
}