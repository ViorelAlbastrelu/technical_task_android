package com.sliide.usermanager.domain

import com.sliide.usermanager.api.UsersService
import com.sliide.usermanager.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.sliide.usermanager.api.model.User as ApiUser

class UsersRepository(
    private val usersService: UsersService
) : UsersRepo {

    private val userListCache: MutableList<User> = mutableListOf()
    override var userListFlow: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())

    override suspend fun getUsersAtLastPage() {
        if (userListCache.isNotEmpty()) {
            userListFlow.emit(userListCache)
        }

        val firstPageResponse = usersService.getUsers(FIRST_PAGE)
        if (firstPageResponse.isSuccessful) {
            val lastPage = firstPageResponse.headers()["X-Pagination-Pages"] ?: FIRST_PAGE
            val lastPageResponse = usersService.getUsers(lastPage)
            val body = lastPageResponse.body()

            when {
                lastPageResponse.isSuccessful && body != null -> {
                    val domainUsers = body.map { it.toDomain() }
                    val noUsersCached = userListCache.isEmpty()
                    val cacheHasBeenUpdated = updateCache(domainUsers)
                    if (noUsersCached || cacheHasBeenUpdated) {
                        userListFlow.emit(domainUsers)
                    }
                }
                else -> throw(Throwable(lastPageResponse.message()))
            }
        } else
            throw(Throwable(firstPageResponse.message()))
    }

    override suspend fun deleteUser(id: Int) {
        val response = usersService.deleteUser(id.toString())
        if (response.isSuccessful) {
            getUsersAtLastPage()
        } else
            throw(Throwable(response.message()))
    }

    override suspend fun addUser(name: String, email: String): Flow<User> {
        return flow {
            val user = com.sliide.usermanager.api.model.User(name = name, email = email)
            val response = usersService.createUser(user)
            val body = response.body()

            if (response.isSuccessful && body != null){
                getUsersAtLastPage()
                emit(body.toDomain())
            }
            else
                throw(Throwable(response.message()))
        }
    }

    private fun updateCache(users: List<User>): Boolean {
        val areDifferentLengths = userListCache.size != users.size
        val aNewItemIsDifferent = userListCache.zip(users).any { (cache, new) -> cache != new }

        if (areDifferentLengths && aNewItemIsDifferent) {
            userListCache.clear()
            userListCache.addAll(users)
            return true
        }
        return false
    }

    private fun generateCreationTime(): String {
        val date: LocalDate = LocalDate.now()
        return date.format(DateTimeFormatter.ISO_DATE) ?: ""
    }

    private fun ApiUser.toDomain(): User {
        val creationTime = generateCreationTime()
        return User(
            this.id,
            this.name,
            this.email,
            creationTime
        )
    }

    companion object {
        const val FIRST_PAGE = "1"
    }
}