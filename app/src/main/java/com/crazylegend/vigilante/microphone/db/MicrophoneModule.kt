package com.crazylegend.vigilante.microphone.db

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
object MicrophoneModule {

    @Provides
    @Singleton
    fun micDatabase(@ApplicationContext context: Context) = MicrophoneDatabase.getInstance(context)

    @Provides
    @Singleton
    fun micDAO(microphoneDatabase: MicrophoneDatabase) = microphoneDatabase.dao()


}