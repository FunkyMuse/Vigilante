package com.crazylegend.vigilante.di.modules

import androidx.fragment.app.Fragment
import com.crazylegend.vigilante.di.qualifiers.FragmentContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    @FragmentContext
    fun provideContext(fragment: Fragment) = fragment.requireContext()

}