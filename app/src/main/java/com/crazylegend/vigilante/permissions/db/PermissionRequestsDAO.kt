package com.crazylegend.vigilante.permissions.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */

@Dao
interface PermissionRequestsDAO {

    @Insert
    suspend fun insertPermissionRequest(permissionRequestModel: PermissionRequestModel)

    @Query("select * from permissionRequests order by dateOfRequest desc")
    fun getAllPermissionRequests(): PagingSource<Int, PermissionRequestModel>

    @Query("select count(*) from permissionRequests")
    fun permissionCountRequest(): Flow<Int>

    @Query("select count(*) from permissionRequests where packageRequestingThePermission is not null and packageRequestingThePermission =:nameOfThePackage")
    fun getPermissionRequestsForPackage(nameOfThePackage: String): Flow<Int>
}