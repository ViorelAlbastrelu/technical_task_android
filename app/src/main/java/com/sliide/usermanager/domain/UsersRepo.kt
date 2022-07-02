package com.sliide.usermanager.domain

import com.sliide.usermanager.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UsersRepo {
    suspend fun getUsersAtPage()
    suspend fun getUser(id: Int): Flow<User>
    suspend fun addUser(name: String, email: String): Flow<User>
    var userListFlow: MutableStateFlow<List<User>>
}