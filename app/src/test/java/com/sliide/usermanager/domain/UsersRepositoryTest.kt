package com.sliide.usermanager.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.sliide.usermanager.MockUserData
import com.sliide.usermanager.api.UsersService
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

    private val mockApiUsersListResponse = MockUserData.mockApiUsersListResponse
    private val mockUserList = MockUserData.mockUserList

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