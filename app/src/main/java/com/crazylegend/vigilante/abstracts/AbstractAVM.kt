package com.crazylegend.vigilante.abstracts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.PagingConfig

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
abstract class AbstractAVM(application: Application) : AndroidViewModel(application) {
    val pagingConfig = PagingConfig(pageSize = 10, enablePlaceholders = false)
}