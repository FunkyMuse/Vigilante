package com.crazylegend.vigilante.permissions

import android.content.Context
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/21/20 to long live and prosper !
 */
@ServiceScoped
class PermissionsManager @Inject constructor(
        @ServiceContext private val context: Context) {


}