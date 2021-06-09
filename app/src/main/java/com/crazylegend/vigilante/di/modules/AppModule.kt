package com.crazylegend.vigilante.di.modules

import android.content.Context
import androidx.work.WorkManager
import com.crazylegend.security.MagiskDetector
import com.crazylegend.security.encryptedSharedPreferences
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by crazy on 11/1/20 to long live and prosper !
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @EncryptedPrefs
    @Singleton
    fun encryptedDefaultSharedPreferences(@ApplicationContext context: Context) =
        context.encryptedSharedPreferences()

    @Provides
    @Singleton
    fun magiskDetector(@ApplicationContext context: Context) = MagiskDetector(context)

    @Provides
    @Singleton
    fun workManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}