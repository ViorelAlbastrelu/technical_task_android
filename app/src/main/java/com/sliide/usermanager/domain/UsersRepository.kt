package com.sliide.usermanager.domain

import com.sliide.usermanager.api.UsersService
import com.sliide.usermanager.domain.model.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.sliide.usermanager.api.model.User as ApiUser

class UsersRepository(
    private val usersService: UsersService
) : UsersRepo {
    private var usersCache: HashMap<Long, User> = hashMapOf()

    override suspend fun getUsersAtPage(page: String): List<User> {
        val apiUsers = usersService.getUsers(page)
        val creationTime = generateCreationTime()
        val domainUsers = apiUsers.map { it.toDomain(creationTime) }

        domainUsers.associateByTo(usersCache) { it.id }
        return domainUsers
    }

    override suspend fun getUser(id: Long): User {
        val cachedUser = usersCache[id]
        if (cachedUser != null) return cachedUser

        usersService.getUserDetails(id.toString()).apply {
            val creationTime = generateCreationTime()
            val domainUser = this.toDomain(creationTime)
            usersCache[id] = domainUser
            return domainUser
        }
    }

    private fun generateCreationTime(): String {
        val date: LocalDate = LocalDate.now()
        return date.format(DateTimeFormatter.ISO_DATE) ?: ""
    }


    private fun ApiUser.toDomain(creationTime: String): User {
        return User(
            this.id,
            this.name,
            this.email,
            creationTime
        )
    }
}