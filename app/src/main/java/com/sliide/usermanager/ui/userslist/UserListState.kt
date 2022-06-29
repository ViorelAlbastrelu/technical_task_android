package com.sliide.usermanager.ui.userslist

import com.sliide.usermanager.model.User

sealed interface UserListState{
    object NoUsers: UserListState
    data class ListUsers(val users: List<User>): UserListState
}