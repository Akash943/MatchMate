package com.example.matchmate.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.matchmate.data.local.UserDao
import com.example.matchmate.data.model.UserProfile
import com.example.matchmate.data.network.UserService

class MatchRepository(
    private val apiService: UserService,
    private val userDao: UserDao
) {

    val allUsers: LiveData<List<UserProfile>> = userDao.getAllUsersLive()

    suspend fun fetchUsersFromApi(page: Int) {
        try {
            val response = apiService.getUsers(page = page)
            val results = response.results.map {
                it.copy(status = "PENDING")
            }
            try {
                userDao.insertUsers(results)
            } catch (dbEx: Exception) {
                Log.e("MatchRepository", "DB insert failed: ${dbEx.message}")
            }
        } catch (e: Exception) {
            Log.e("MatchRepository", "API fetch failed: ${e.message}")
            throw e
        }
    }

    suspend fun updateUserStatus(email: String, newStatus: String) {
        try {
            val user = userDao.getUserByEmail(email)
            user?.let {
                val updatedUser = it.copy(status = newStatus, isSynced = false)
                userDao.updateUser(updatedUser)
            }
        } catch (e: Exception) {
            Log.e("MatchRepository", "Failed to update status: ${e.message}")
        }
    }
}
