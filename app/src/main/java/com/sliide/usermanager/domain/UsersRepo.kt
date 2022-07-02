package com.sliide.usermanager.domain

import com.sliide.usermanager.domain.model.User

interface UsersRepo {
    suspend fun getUsersAtPage(page: String): List<User>
    suspend fun getUser(id: Long): User
}