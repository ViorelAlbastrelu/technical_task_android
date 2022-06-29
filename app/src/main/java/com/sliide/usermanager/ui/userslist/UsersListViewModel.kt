package com.sliide.usermanager.ui.userslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.usermanager.domain.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
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
            val usersAtPage = usersRepository.getUsersAtPage("1")
            if (usersAtPage.isNotEmpty())
                _state.value = UserListState.ListUsers(usersAtPage)
        }
    }

}