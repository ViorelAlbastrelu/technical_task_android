package com.sliide.usermanager.api

import com.sliide.usermanager.api.model.User
import retrofit2.Response
import retrofit2.http.*

interface UsersService {

    @GET("users")
    suspend fun getUsers(@Query("page") page: String): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUserDetails(@Path("id") id: String): Response<User>

    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    @PUT("users/{id}")
    suspend fun updateUser(user: User)

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String)
}