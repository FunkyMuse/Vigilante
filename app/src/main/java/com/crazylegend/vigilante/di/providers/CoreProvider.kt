package com.crazylegend.vigilante.di.providers

import android.app.Application
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */

@Singleton
class CoreProvider @Inject constructor(@ApplicationContext application: Application) {

}