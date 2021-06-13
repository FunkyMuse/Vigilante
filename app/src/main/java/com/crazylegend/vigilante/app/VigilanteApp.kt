package com.crazylegend.vigilante.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.crazylegend.kotlinextensions.locale.LocaleHelper
import com.crazylegend.vigilante.di.providers.CoreProvider
import com.crazylegend.vigilante.di.providers.prefs.defaultPrefs.DefaultPreferencessProvider
import com.crazylegend.vigilante.utils.DEFAULT_LANGUAGE
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
@HiltAndroidApp
class VigilanteApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    @Inject
    lateinit var coreProvider: CoreProvider

    @Inject
    lateinit var prefsProvider: DefaultPreferencessProvider

    override fun onCreate() {
        super.onCreate()
        prefsProvider.applyThemeLogic()
        coreProvider.apply {
            setVMPolicy()
            setThreadPolicy()
            checkAppId()
            checkIfIsDebuggable()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        base?.apply { LocaleHelper.onAttach(this, DEFAULT_LANGUAGE) }
    }
}