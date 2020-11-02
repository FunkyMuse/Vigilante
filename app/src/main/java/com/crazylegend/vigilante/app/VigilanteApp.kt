package com.crazylegend.vigilante.app

import android.app.Application
import com.crazylegend.vigilante.di.providers.CoreProvider
import com.crazylegend.vigilante.di.providers.PrefsProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
@HiltAndroidApp
class VigilanteApp : Application() {

    @Inject
    lateinit var coreProvider: CoreProvider

    @Inject
    lateinit var prefsProvider: PrefsProvider

    override fun onCreate() {
        super.onCreate()
        prefsProvider.applyThemeLogic()
        coreProvider.apply {
            setVMPolicy()
            setThreadPolicy()
        }
    }
}