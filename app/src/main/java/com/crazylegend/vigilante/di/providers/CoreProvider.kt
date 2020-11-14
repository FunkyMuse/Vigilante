package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.os.StrictMode
import com.crazylegend.security.isDebuggable
import com.crazylegend.vigilante.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.system.exitProcess

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@Singleton
class CoreProvider @Inject constructor(@ApplicationContext private val context: Context) {

    fun setVMPolicy() {
        if (BuildConfig.DEBUG) {
            StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .build()
        }
    }

    fun setThreadPolicy() {
        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .build()
        }
    }

    fun checkAppId() {
        if (BuildConfig.APPLICATION_ID != "com.crazylegend.vigilante") {
            exitProcess(-1)
        }
    }

    fun checkIfIsDebuggable() {
        if (!BuildConfig.DEBUG && context.isDebuggable()) {
            exitProcess(-1)
        }
    }


}