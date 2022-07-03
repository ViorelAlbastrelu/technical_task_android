package com.sliide.usermanager.ui.userslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sliide.usermanager.MainCoroutineRule
import com.sliide.usermanager.MockUserData
import com.sliide.usermanager.domain.UsersRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class UserListViewModelTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var usersRepo: UsersRepo

    @Mock
    lateinit var stateObserver: Observer<UserListState>

    private lateinit var sut: UsersListViewModel

    private val mockUserList = MockUserData.mockUserList

    @Before
    fun setUp() {
        sut = UsersListViewModel(usersRepo)
        sut.state.observeForever(stateObserver)
    }

    @Test
    fun `given users repository is fetching users when function is called then ui state is Loading`() = runBlocking {
        //Given
        whenever(usersRepo.userListFlow).thenReturn(MutableStateFlow(emptyList()))

        //When
        sut.fetchUsers()

        //Then
        verify(stateObserver).onChanged(UserListState.Loading(true))
    }

    @Test
    fun `given users repository is fetching users when the list is empty then ui state is NoUsers`() = runBlocking {
        //Given
        whenever(usersRepo.userListFlow).thenReturn(MutableStateFlow(emptyList()))

        //When
        sut.fetchUsers()
        val uiState = sut.state.value

        //Then
        Assert.assertEquals(UserListState.NoUsers, uiState)
    }

    @Test
    fun `given users repository is fetching users when the list has users then ui state is ListUsers`() = runBlocking {
        //Given
        whenever(usersRepo.userListFlow).thenReturn(MutableStateFlow(mockUserList))

        //When
        sut.fetchUsers()
        val uiState = sut.state.value

        //Then
        Assert.assertEquals(UserListState.ListUsers(mockUserList), uiState)
    }


    @Test
    fun `given when then`() = runBlocking {
        //Given


        //When


        //Then
    }

}