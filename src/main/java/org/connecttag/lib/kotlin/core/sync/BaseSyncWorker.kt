package org.connecttag.lib.kotlin.core.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import timber.log.Timber

abstract class BaseSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    abstract suspend fun doSync(): ListenableWorker.Result

    override suspend fun doWork(): ListenableWorker.Result {
        val workerName = this::class.java.simpleName
        Timber.tag("SyncWorker").d("Starting sync: $workerName")
        
        return try {
            val result = doSync()
            Timber.tag("SyncWorker").d("Sync finished: $workerName with result: $result")
            result
        } catch (e: Exception) {
            Timber.tag("SyncWorker").e(e, "Sync failed: $workerName")
            ListenableWorker.Result.retry()
        }
    }
}
