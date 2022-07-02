package com.sliide.usermanager.ui.userslist

import com.sliide.usermanager.domain.model.User

sealed interface UserListState {
    object NoUsers: UserListState
    data class Loading(val isLoading: Boolean) : UserListState
    data class ListUsers(val users: List<User>) : UserListState
    data class Error(val error: String) : UserListState
}