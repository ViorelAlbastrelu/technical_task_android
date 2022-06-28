package com.sliide.usermanager.domain

import com.sliide.usermanager.api.UsersService
import com.sliide.usermanager.model.User

class UsersRepository(
    private val usersService: UsersService
) {
    private val usersCache: HashMap<String, User> = hashMapOf()

    suspend fun getUser(id: String): User {
        val cachedUser = usersCache[id]
        if (cachedUser != null)
            return cachedUser

        usersService.getUserDetails(id).apply {
            usersCache[id] = this
            return this
        }
    }
}