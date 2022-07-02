package com.sliide.usermanager.ui.userslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.usermanager.domain.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersListViewModel @Inject constructor(
    private val usersRepository: UsersRepo
) : ViewModel() {

    private val _state: MutableLiveData<UserListState> = MutableLiveData()
    val state: LiveData<UserListState>
        get() = _state

    fun fetchUsers(page: Int) {
        viewModelScope.launch {
            loading()
            delay(2000) // added to see loading
            val usersAtPage = usersRepository.getUsersAtPage(page.toString())
            if (usersAtPage.isNotEmpty()){
                loading(false)
                _state.value = UserListState.ListUsers(usersAtPage)
            } else {
                _state.value = UserListState.NoUsers
            }
        }
    }

    private fun loading(isLoading: Boolean = true) {
        _state.value = UserListState.Loading(isLoading)
    }
}