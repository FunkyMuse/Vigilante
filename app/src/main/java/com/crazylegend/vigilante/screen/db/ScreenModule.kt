package com.crazylegend.vigilante.screen.db

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@Module
@InstallIn(SingletonComponent::class)
object ScreenModule {

    @Provides
    @Singleton
    fun screenDatabase(@ApplicationContext context: Context) = ScreenDatabase.getInstance(context)

    @Provides
    @Singleton
    fun screenDAO(screenDatabase: ScreenDatabase) = screenDatabase.dao()


}