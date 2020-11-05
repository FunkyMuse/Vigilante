package com.crazylegend.vigilante.permissions.requests.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */

@Dao
interface PermissionRequestsDAO {

    @Insert
    suspend fun insertPermissionRequest(permissionRequestModel: PermissionRequestModel)

    @Query("select * from permissionRequests")
    fun getAllPermissionRequests(): PagingSource<Int, PermissionRequestModel>

}