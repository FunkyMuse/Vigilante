package com.crazylegend.vigilante.screen.db

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
@Singleton
class ScreenRepository @Inject constructor(private val dao: ScreenDAO) : ScreenDAO by dao