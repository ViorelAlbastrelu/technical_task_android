package com.sliide.usermanager.domain

import com.sliide.usermanager.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface UsersRepo {
    suspend fun getUsersAtLastPage()
    suspend fun deleteUser(id: Int)
    suspend fun addUser(name: String, email: String): Flow<User>
    var userListFlow: MutableStateFlow<List<User>>
}