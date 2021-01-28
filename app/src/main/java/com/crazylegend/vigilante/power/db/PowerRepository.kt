package com.crazylegend.vigilante.power.db

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 11/7/20 to long live and prosper !
 */
@Singleton
class PowerRepository @Inject constructor(private val powerDAO: PowerDAO) : PowerDAO by powerDAO