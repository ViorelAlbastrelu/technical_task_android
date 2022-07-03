package com.sliide.usermanager.ui.userslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.usermanager.domain.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val usersRepository: UsersRepo
) : ViewModel() {

    private val _state: MutableLiveData<UserListState> = MutableLiveData()
    val state: LiveData<UserListState>
        get() = _state

    fun fetchUsers() {
        viewModelScope.launch {
            loading()
            delay(2000) // added to see loading

            try {
                usersRepository.getUsersAtPage()
            } catch (throwable: Throwable) {
                throwable.message?.let { _state.value = UserListState.Error(it) }
            }
            usersRepository.userListFlow.collect { usersAtPage ->
                loading(false)
                if (usersAtPage.isNotEmpty()){
                    _state.value = UserListState.ListUsers(usersAtPage)
                } else {
                    _state.value = UserListState.NoUsers
                }
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            try {
                usersRepository.deleteUser(id)
            } catch (throwable: Throwable) {
                throwable.message?.let { _state.value = UserListState.Error(it) }
            }
        }
    }

    private fun loading(isLoading: Boolean = true) {
        _state.value = UserListState.Loading(isLoading)
    }
}