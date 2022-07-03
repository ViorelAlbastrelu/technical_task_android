package com.sliide.usermanager.ui.useradd

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.usermanager.domain.UsersRepo
import com.sliide.usermanager.ui.userslist.UserListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddViewModel @Inject constructor(
    private val usersRepository: UsersRepo
) : ViewModel() {

    private val _state: MutableLiveData<UserAddState> = MutableLiveData()
    val state: LiveData<UserAddState>
        get() = _state

    fun addUser(name: String, email: String) {
        viewModelScope.launch {
            try {
                usersRepository.addUser(name, email).collect {
                    _state.value = UserAddState.Success
                }
            } catch (throwable: Throwable) {
                throwable.message?.let { _state.value = UserAddState.Error(it)  }
            }
        }
    }

    fun validateInputFields(name: String, email: String) {
        val addAllowed = isNameEmail(name) && isValidEmail(email)
        _state.value = UserAddState.AllowAdd(addAllowed)
    }

    private fun isNameEmail(name: CharSequence): Boolean {
        return if (TextUtils.isEmpty(name)) {
            false
        } else {
            name.length >= MIN_NAME_LENGTH
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    companion object {
        private const val MIN_NAME_LENGTH = 4
    }
}