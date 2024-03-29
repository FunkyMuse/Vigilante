package com.crazylegend.vigilante.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun vigilanteDatabase(@ApplicationContext context: Context) = VigilanteDatabase.getInstance(context)

    @Provides
    @Singleton
    fun headsetDao(database: VigilanteDatabase) = database.headsetDao()

    @Provides
    @Singleton
    fun notificationsDao(database: VigilanteDatabase) = database.notificationsDAO()

    @Provides
    @Singleton
    fun screenDao(database: VigilanteDatabase) = database.screenDAO()

    @Provides
    @Singleton
    fun permissionRequestsDao(database: VigilanteDatabase) = database.permissionRequestsDAO()

    @Provides
    @Singleton
    fun powerDAO(database: VigilanteDatabase) = database.powerDAO()

}