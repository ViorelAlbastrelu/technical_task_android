package com.sliide.usermanager.api

import com.sliide.usermanager.model.User
import retrofit2.http.*

interface UsersService {

    @GET("v2/users?page={pageNumber}")
    suspend fun getUsers(@Path("pageNumber") page: String): List<User>

    @GET("v2/users/{id}")
    suspend fun getUserDetails(@Path("id") id: String): User

    @POST("v2/users")
    suspend fun createUser(user: User)

    @PUT("v2/users/{id}")
    suspend fun updateUser(user: User)

    @DELETE("v2/users/{id}")
    suspend fun deleteUser(@Path("id") id: String)
}