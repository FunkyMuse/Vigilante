package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.os.StrictMode
import com.crazylegend.vigilante.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@Singleton
class CoreProvider @Inject constructor(@ApplicationContext context: Context) {

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


}