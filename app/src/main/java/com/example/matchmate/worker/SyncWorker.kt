package com.example.matchmate.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.matchmate.MatchMateApplication
import com.example.matchmate.data.model.UserProfile

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val userDao = MatchMateApplication.database.userDao()

    override suspend fun doWork(): Result {
        return try {
            val unsyncedUsers: List<UserProfile> = userDao.getUnsyncedUsers()

            if (unsyncedUsers.isNotEmpty()) {
                Log.d("SyncWorker", "Syncing ${unsyncedUsers.size} users...")
                val syncedUsers = unsyncedUsers.map { it.copy(isSynced = true) }
                userDao.updateUsers(syncedUsers)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error syncing users: ${e.message}")
            Result.retry()
        }
    }
}
