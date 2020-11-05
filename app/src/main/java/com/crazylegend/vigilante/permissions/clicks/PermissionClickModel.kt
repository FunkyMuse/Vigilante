package com.crazylegend.vigilante.permissions.clicks


import com.crazylegend.kotlinextensions.currentTimeMillis
import com.crazylegend.vigilante.permissions.PermissionType
import java.util.*

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */

data class PermissionClickModel(
        val packageRequestingThePermission: String?,
        val permissionMessage: String?,
        val date: Date = Date(currentTimeMillis),
        val permissionType: Int = 0,
        val permissionClickType: String
) {

    /**
     * 0 - denied
     * 1 - allowed
     * 2- ask every time
     */
    val permissionAllowanceType get() = permissionType

    val permissionActionType get() = PermissionType.valueOf(permissionClickType)

}