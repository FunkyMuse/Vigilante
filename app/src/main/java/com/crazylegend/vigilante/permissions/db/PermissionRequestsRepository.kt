package com.crazylegend.vigilante.permissions.db

import javax.inject.Inject

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
class PermissionRequestsRepository @Inject constructor(private val permissionRequestDAO: PermissionRequestsDAO) {

    fun getAllRequests() = permissionRequestDAO.getAllPermissionRequests()
    fun totalRequests() = permissionRequestDAO.permissionCountRequest()

    fun getPermissionCountForPackage(packageName: String) = permissionRequestDAO.getPermissionRequestsForPackage(packageName)

    suspend fun insertPermissionRequest(permissionRequestModel: PermissionRequestModel) = permissionRequestDAO.insertPermissionRequest(permissionRequestModel)
}