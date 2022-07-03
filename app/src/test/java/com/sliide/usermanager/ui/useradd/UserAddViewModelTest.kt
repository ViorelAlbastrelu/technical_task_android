package com.sliide.usermanager.ui.useradd

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sliide.usermanager.MainCoroutineRule
import com.sliide.usermanager.MockUserData
import com.sliide.usermanager.domain.UsersRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
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
internal class UserAddViewModelTest{

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var usersRepo: UsersRepo

    @Mock
    lateinit var stateObserver: Observer<UserAddState>

    private lateinit var sut: UserAddViewModel

    @Before
    fun setUp() {
        sut = UserAddViewModel(usersRepo)
        sut.state.observeForever(stateObserver)
    }

    @Test
    fun `given a user will be created when input fields are empty then add button is disabled`() = runBlocking {
        //Given

        //When
        sut.validateInputFields("", "")

        //Then
        verify(stateObserver).onChanged(UserAddState.AllowAdd(false))
    }

    @Test
    fun `given a user will be created when name input field is empty then add button is disabled`() = runBlocking {
        //Given

        //When
        sut.validateInputFields("", "test@email.com")

        //Then
        verify(stateObserver).onChanged(UserAddState.AllowAdd(false))
    }

    @Test
    fun `given a user will be created when email input field is empty then add button is disabled`() = runBlocking {
        //Given

        //When
        sut.validateInputFields("test", "")

        //Then
        verify(stateObserver).onChanged(UserAddState.AllowAdd(false))
    }

    @Test
    fun `given a user will be created when name input field is below 4 char then add button is disabled`() = runBlocking {
        //Given

        //When
        sut.validateInputFields("tes", "test@email.com")

        //Then
        verify(stateObserver).onChanged(UserAddState.AllowAdd(false))
    }

    @Test
    fun `given a user will be created when email does not match pattern then add button is disabled`() = runBlocking {
        //Given

        //When
        sut.validateInputFields("test", "testemail.com@")

        //Then
        verify(stateObserver).onChanged(UserAddState.AllowAdd(false))
    }

    @Test
    fun `given a user will be created when name and email are valid then add button is enabled`() = runBlocking {
        //Given

        //When
        sut.validateInputFields("test", "test@email.com")

        //Then
        verify(stateObserver).onChanged(UserAddState.AllowAdd(true))
    }

    @Test
    fun `given a user will be created when add is tapped then ui state is success to dismiss dialog`() = runBlocking {
        //Given
        whenever(usersRepo.addUser(any(), any())).thenReturn(flow { emit(MockUserData.mockUserList[0]) })

        //When
        sut.addUser("test", "test@email.com")

        //Then
        verify(stateObserver).onChanged(UserAddState.Success)
    }
}