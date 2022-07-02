package com.sliide.usermanager.ui.useradd

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.usermanager.domain.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddViewModel @Inject constructor(
    private val usersRepository: UsersRepo
) : ViewModel() {

    private val _state: MutableLiveData<UserAddState> = MutableLiveData()
    val state: LiveData<UserAddState>
        get() = _state

    fun addUser(name: String?, email: String?) {
        viewModelScope.launch {
            usersRepository.addUser(name!!, email!!)
            _state.value = UserAddState.Success
        }
    }

    fun validateInputFields(name: String, email: String) {
        val addAllowed = isNameEmail(name) && isValidEmail(email)
        _state.value = UserAddState.AllowAdd(addAllowed)
    }

    fun isNameEmail(name: CharSequence): Boolean {
        return if (TextUtils.isEmpty(name)) {
            false
        } else {
            name.length >= MIN_NAME_LENGTH
        }
    }

    fun isValidEmail(email: CharSequence): Boolean {
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