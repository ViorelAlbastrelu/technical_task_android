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

    private var usersCache: HashMap<Int, User> = hashMapOf()
    override var userListFlow: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())

    override suspend fun getUsersAtPage() {
        val firstPageResponse = usersService.getUsers(FIRST_PAGE)
        if (firstPageResponse.isSuccessful) {
            val lastPage = firstPageResponse.headers()["X-Pagination-Pages"] ?: FIRST_PAGE
            val lastPageResponse = usersService.getUsers(FIRST_PAGE) //TODO replace with last page
            val body = lastPageResponse.body()

            when {
                lastPageResponse.isSuccessful && body != null -> {
                    val domainUsers = body.map { it.toDomain() }
                    domainUsers.associateByTo(usersCache) { it.id }
                    userListFlow.emit(domainUsers)
                }
                usersCache.isNotEmpty() -> userListFlow.emit(usersCache.values.toList())
                else -> throw(Throwable(lastPageResponse.message()))
            }
        } else
            throw(Throwable(firstPageResponse.message()))
    }

    override suspend fun getUser(id: Int): Flow<User> {
        val cachedUser = usersCache[id]

        return flow {
            if (cachedUser != null) emit(cachedUser)
            val response = usersService.getUserDetails(id.toString())
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val domainUser = body.toDomain()
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
            val body = response.body()

            if (response.isSuccessful && body != null){
                getUsersAtPage()
                emit(body.toDomain())
            }
            else
                throw(Throwable(response.message()))
        }
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