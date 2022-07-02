package com.sliide.usermanager.domain

import com.sliide.usermanager.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepo {
    suspend fun getUsersAtPage(page: String): Flow<List<User>>
    suspend fun getUser(id: Int): Flow<User>
    suspend fun addUser(name: String, email: String): Flow<User>
}