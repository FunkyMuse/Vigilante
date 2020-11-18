package com.crazylegend.vigilante.app

import android.app.Application
import android.content.Context
import com.crazylegend.kotlinextensions.locale.LocaleHelper
import com.crazylegend.vigilante.di.providers.CoreProvider
import com.crazylegend.vigilante.di.providers.PrefsProvider
import com.crazylegend.vigilante.utils.DEFAULT_LANGUAGE
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
            checkAppId()
            checkIfIsDebuggable()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        base?.apply { LocaleHelper.onAttach(this, DEFAULT_LANGUAGE) }
    }
}