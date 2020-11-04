package com.crazylegend.vigilante.notifications.db

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
object NotificationsModule {

    @Provides
    @Singleton
    fun notificationsDatabase(@ApplicationContext context: Context) = NotificationsDatabase.getInstance(context)

    @Provides
    @Singleton
    fun notificationsDAO(notificationsDatabase: NotificationsDatabase) = notificationsDatabase.dao()
}