package com.sliide.usermanager.ui.useradd

sealed interface UserAddState {
    data class AllowAdd(val enable: Boolean) : UserAddState
    data class Error(val error: String) : UserAddState
    object Success: UserAddState
}