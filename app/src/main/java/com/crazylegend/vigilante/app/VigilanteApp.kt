package com.crazylegend.vigilante.app

import android.app.Application
import com.crazylegend.vigilante.di.providers.CoreProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
@HiltAndroidApp
class VigilanteApp : Application() {

    @Inject
    lateinit var coreProvider: CoreProvider

    override fun onCreate() {
        super.onCreate()
        coreProvider.apply {
            setVMPolicy()
            setThreadPolicy()
        }
    }
}