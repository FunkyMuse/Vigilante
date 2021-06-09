package com.crazylegend.vigilante.historyDeletionWorker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.crazylegend.vigilante.database.VigilanteDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Created by funkymuse on 6/9/21 to long live and prosper !
 */
@HiltWorker
class HistoryDeletionWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val database: VigilanteDatabase
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_ID = "historyDeletionWorkID"
    }

    override suspend fun doWork(): Result {
        database.clearAllTables()
        return Result.success()
    }
}