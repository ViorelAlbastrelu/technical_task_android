package com.sliide.usermanager.domain

import com.sliide.usermanager.api.UsersService
import com.sliide.usermanager.domain.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.sliide.usermanager.api.model.User as ApiUser

class UsersRepository(
    private val usersService: UsersService
) : UsersRepo {
    private var usersCache: HashMap<Int, User> = hashMapOf()

    override suspend fun getUsersAtPage(page: String): Flow<List<User>> {
        val response = usersService.getUsers(page)
        val body = response.body()

        return flow {
            if (response.isSuccessful && body != null) {
                val creationTime = generateCreationTime()
                val domainUsers = body.map { it.toDomain(creationTime) }
                domainUsers.associateByTo(usersCache) { it.id }
                emit(domainUsers)
            } else if (usersCache.isNotEmpty()) {
                emit(usersCache.values.toList())
            } else
                throw(Throwable(response.message()))
        }
    }

    override suspend fun getUser(id: Int): Flow<User> {
        val cachedUser = usersCache[id]

        return flow {
            if (cachedUser != null) emit(cachedUser)
            val response = usersService.getUserDetails(id.toString())
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val creationTime = generateCreationTime()
                val domainUser = body.toDomain(creationTime)
                usersCache[id] = domainUser
                emit(domainUser)
            } else
                throw(Throwable(response.message()))
        }
    }

    override suspend fun addUser(name: String, email: String): Flow<User> {
        return flow {
            val user = com.sliide.usermanager.api.model.User(name = name, email = email)
            val response = usersService.createUser(user)
            if (response.isSuccessful) {
                val creationTime = generateCreationTime()
                val body = response.body()
                if (body != null) {
                    emit(body.toDomain(creationTime))
                }
            }
            else
                throw(Throwable(response.message()))
        }
//        currentCoroutineContext().cancel(CancellationException(response.message()))
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