package com.crazylegend.vigilante.di.modules

import android.app.Service
import android.content.Context
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
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
    @ServiceContext
    fun serviceContext(service: Service): Context = service.applicationContext


}