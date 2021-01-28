package com.crazylegend.vigilante.permissions.db

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@Singleton
class PermissionRequestsRepository @Inject constructor(private val permissionRequestDAO: PermissionRequestsDAO) : PermissionRequestsDAO by permissionRequestDAO