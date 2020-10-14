package com.crazylegend.vigilante.di.modules

import android.app.Service
import android.content.Context
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.di.scopes.PerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @PerService
    @ServiceContext
    fun serviceContext(service: Service): Context = service.applicationContext


}