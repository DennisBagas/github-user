package com.example.githubproject.data.api

import com.example.githubproject.model.userData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GetApi {

    @GET("users")
    @Headers("Authorization: 5f66e43e5f6d841bd94d3147af76f0ab72b95225")
    fun getUser(): Call<List<userData>>

    @GET("users/{login}")
    @Headers("Authorization: 5f66e43e5f6d841bd94d3147af76f0ab72b95225")
    fun getUserProfile(
        @Path("login") userId: String
    ) : Call<userData>

    @GET("users/{login}/followers")
    @Headers("Authorization: 5f66e43e5f6d841bd94d3147af76f0ab72b95225")
    fun getUserProfileFollowers(
            @Path("login") userId: String
    ) : Call<List<userData>>

    @GET("users/{login}/following")
    @Headers("Authorization: 5f66e43e5f6d841bd94d3147af76f0ab72b95225")
    fun getUserProfileFollowing(
            @Path("login") userId: String
    ) : Call<List<userData>>

}