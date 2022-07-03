package com.sliide.usermanager.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.sliide.usermanager.api.UsersService
import com.sliide.usermanager.domain.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val name0 = "User0"
const val name1 = "User1"
const val name2 = "User2"

@RunWith(MockitoJUnitRunner::class)
internal class UsersRepositoryTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var usersService: UsersService

    private lateinit var sut: UsersRepo

    private val localDate: String
        get() {
            val date: LocalDate = LocalDate.now()
            return date.format(DateTimeFormatter.ISO_DATE) ?: ""
        }

    private val mockApiUsersListResponse = listOf(
        com.sliide.usermanager.api.model.User(0, name0, "$name0@email.com"),
        com.sliide.usermanager.api.model.User(1, name1, "$name1@email.com"),
        com.sliide.usermanager.api.model.User(2, name2, "$name2@email.com"),
    )

    private val mockUserList = listOf(
        User(0, name0, "$name0@email.com", localDate),
        User(1, name1, "$name1@email.com", localDate),
        User(2, name2, "$name2@email.com", localDate)
    )

    @Before
    fun setUp() {
        sut = UsersRepository(usersService)
    }

    @Test
    fun `given user service returns api users when repository is fetching users then return domain users`() = runBlocking {
        //Given
        whenever(usersService.getUsers(any())).thenReturn(Response.success(mockApiUsersListResponse))

        //When
        sut.getUsersAtLastPage()

        //Then
        val first = sut.userListFlow.first()
        Assert.assertEquals(mockUserList, first)
    }

    @Test
    fun `given a user is deleted when delete service is called then refresh users list`() = runBlocking {
        //Given
        whenever(usersService.getUsers(any())).thenReturn(Response.success(mockApiUsersListResponse))
        whenever(usersService.deleteUser(any())).thenReturn(Response.success(mockApiUsersListResponse[0]))

        //When
        sut.deleteUser(any())

        //Then
        val first = sut.userListFlow.first()
        Assert.assertEquals(mockUserList, first)
    }

    @Test
    fun `given a user is created when create service is successful then refresh users list`() = runBlocking {
        //Given
        whenever(usersService.getUsers(any())).thenReturn(Response.success(mockApiUsersListResponse))
        whenever(usersService.createUser(any())).thenReturn(Response.success(mockApiUsersListResponse[0]))

        //When
        sut.addUser("Name", "Email").collect()

        //Then
        val first = sut.userListFlow.first()
        Assert.assertEquals(mockUserList, first)
    }
}