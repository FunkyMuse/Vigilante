package com.crazylegend.vigilante.camera.db

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {

    @Provides
    @Singleton
    fun cameraDatabase(@ApplicationContext context: Context) = CameraDatabase.getInstance(context)

    @Provides
    @Singleton
    fun cameraDAO(cameraDatabase: CameraDatabase) = cameraDatabase.dao()


}