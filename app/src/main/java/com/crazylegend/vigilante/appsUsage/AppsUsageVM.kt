package com.crazylegend.vigilante.appsUsage

import android.app.Application
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.context.usageStatsManager
import com.crazylegend.kotlinextensions.livedata.context
import com.crazylegend.vigilante.abstracts.AbstractAVM
import java.util.*

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */
class AppsUsageVM(application: Application) : AbstractAVM(application) {

    val data: MutableLiveData<List<UsageStats>> = MutableLiveData()

    init {
        loadInitial()
    }

    fun loadInitial() {
        val startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.HOUR_OF_DAY, -24)
        val endCalendar = Calendar.getInstance()
        val queryUsageStats = context.usageStatsManager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                startCalendar.timeInMillis, endCalendar.timeInMillis)
        data.postValue(queryUsageStats)
    }
}