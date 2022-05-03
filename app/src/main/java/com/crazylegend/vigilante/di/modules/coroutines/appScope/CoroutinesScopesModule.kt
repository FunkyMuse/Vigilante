package com.crazylegend.vigilante.di.modules.coroutines.appScope

import com.crazylegend.vigilante.di.modules.coroutines.dispatchers.DefaultDispatcher
import com.crazylegend.vigilante.di.modules.coroutines.dispatchers.MainImmediateDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Created by funkymuse on 6/19/21 to long live and prosper !
 */

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Singleton
    @Provides
    @ApplicationScope
    fun providesCoroutineScope(@MainImmediateDispatcher defaultDispatcher: CoroutineDispatcher): CoroutineScope =
            CoroutineScope(SupervisorJob() + defaultDispatcher)

}


