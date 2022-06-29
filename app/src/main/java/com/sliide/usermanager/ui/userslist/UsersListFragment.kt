package com.sliide.usermanager.ui.userslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.sliide.usermanager.core.BindingFragment
import com.sliide.usermanager.databinding.FragmentUsersListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersListFragment : BindingFragment<FragmentUsersListBinding>() {

    private val viewModel: UsersListViewModel by viewModels()
    private val usersAdapter = UsersAdapter()

    override fun inflateBinding(inflater: LayoutInflater): FragmentUsersListBinding {
        return FragmentUsersListBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userList.adapter = usersAdapter
        viewModel.fetchUsers()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserListState.ListUsers -> {
                    usersAdapter.submitList(state.users)
                }
                UserListState.NoUsers -> {
                    binding.userList.isGone = true
                }
            }
        }
    }
}