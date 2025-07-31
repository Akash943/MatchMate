package com.example.matchmate.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.matchmate.data.model.UserProfile

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserProfile>)

    @Update
    suspend fun updateUser(user: UserProfile)

    @Query("SELECT * FROM USER_PROFILE")
    fun getAllUsersLive(): LiveData<List<UserProfile>>

    @Query("DELETE FROM USER_PROFILE")
    suspend fun clearAll()

    @Query("SELECT * FROM USER_PROFILE")
    suspend fun getAllUsersOnce(): List<UserProfile>

    @Query("SELECT * FROM user_profile WHERE isSynced = 0")
    suspend fun getUnsyncedUsers(): List<UserProfile>

    @Update
    suspend fun updateUsers(users: List<UserProfile>)

    @Query("SELECT * FROM user_profile WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserProfile?
}
