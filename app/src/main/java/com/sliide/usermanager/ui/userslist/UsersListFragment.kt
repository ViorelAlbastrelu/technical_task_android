package com.sliide.usermanager.ui.userslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.sliide.usermanager.core.AlertDialogFactory
import com.sliide.usermanager.core.BindingFragment
import com.sliide.usermanager.databinding.FragmentUsersListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersListFragment : BindingFragment<FragmentUsersListBinding>(), View.OnLongClickListener {

    private val usersAdapter = UsersAdapter(this)
    private val viewModel: UsersListViewModel by viewModels()
    private var userAlert: AlertDialog? = null

    override fun inflateBinding(inflater: LayoutInflater): FragmentUsersListBinding {
        return FragmentUsersListBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userList.adapter = usersAdapter
        viewModel.fetchUsers()
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserListState.Loading -> showLoading(state.isLoading)
                is UserListState.Error -> showErrorDialog(
                    state.error,
                    AlertDialogFactory.Type.USER_SERVICE_ERROR
                ) { viewModel.fetchUsers() }
                is UserListState.ListUsers -> {
                    usersAdapter.submitList(state.users)
                    binding.userList.smoothScrollToPosition(0)
                }
                UserListState.NoUsers -> showErrorDialog(
                    type = AlertDialogFactory.Type.NO_USERS_ERROR
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissUserAlert()
        userAlert = null
    }

    override fun onLongClick(view: View): Boolean {
        AlertDialogFactory.create(requireContext(), AlertDialogFactory.Type.DELETE_USER_ALERT) {
            viewModel.deleteUser(view.tag as Int)
        }
        return true
    }

    private fun showLoading(visible: Boolean) {
        binding.apply {
            progressBar.isVisible = visible
            userList.isVisible = !visible
        }
    }

    private fun showErrorDialog(
        error: String = "",
        type: AlertDialogFactory.Type,
        positiveAction: () -> Unit = {}
    ) {
        try {
            dismissUserAlert()
            userAlert = AlertDialogFactory.create(requireContext(), type, error, positiveAction)
        } catch (exception: IllegalStateException) {
            dismissUserAlert()
        }
    }

    private fun dismissUserAlert() {
        if (userAlert?.isShowing == true) userAlert?.dismiss()
    }
}