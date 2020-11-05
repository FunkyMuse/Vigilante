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
    fun cameraDao(database: VigilanteDatabase) = database.cameraDao()

    @Provides
    @Singleton
    fun notificationsDao(database: VigilanteDatabase) = database.notificationsDAO()

    @Provides
    @Singleton
    fun microphoneDao(database: VigilanteDatabase) = database.microphoneDAO()

    @Provides
    @Singleton
    fun screenDao(database: VigilanteDatabase) = database.screenDAO()


}