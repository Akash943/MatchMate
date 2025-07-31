package com.example.matchmate.data.network

import com.example.matchmate.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("api/")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") results: Int = 10,
        @Query("seed") seed: String = "matchmate"
    ): UserResponse
}
