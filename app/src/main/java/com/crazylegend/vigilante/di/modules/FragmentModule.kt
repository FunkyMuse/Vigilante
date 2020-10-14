package com.crazylegend.vigilante.di.modules

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
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

    @Provides
    fun viewLifecycleOwner(fragment: Fragment): LifecycleOwner = fragment.viewLifecycleOwner

    @Provides
    fun contentResolver(@FragmentContext context: Context) = context.contentResolver


}