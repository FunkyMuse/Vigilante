package com.crazylegend.vigilante.headset.database

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by crazy on 11/5/20 to long live and prosper !
 */
@Singleton
class HeadsetRepository @Inject constructor(private val headsetDAO: HeadsetDAO) : HeadsetDAO by headsetDAO