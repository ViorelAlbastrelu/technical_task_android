package com.sliide.usermanager.ui.useradd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.sliide.usermanager.core.AlertDialogFactory
import com.sliide.usermanager.core.BindingDialogFragment
import com.sliide.usermanager.databinding.FragmentUserAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAddFragment : BindingDialogFragment<FragmentUserAddBinding>() {

    private val viewModel: UserAddViewModel by viewModels()

    override fun inflateBinding(inflater: LayoutInflater): FragmentUserAddBinding {
        return FragmentUserAddBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            editTextTextPersonName.doAfterTextChanged { validateInputFields() }
            editTextTextEmailAddress.doAfterTextChanged { validateInputFields() }
            buttonAdd.setOnClickListener {
                val inputFields = extractInputFields()
                viewModel.addUser(inputFields.name, inputFields.email)
            }
            buttonCancel.setOnClickListener { dismiss() }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserAddState.AllowAdd -> enableAdd(state.enable)
                UserAddState.Success -> dismiss()
                is UserAddState.Error -> {
                    AlertDialogFactory.create(
                        requireContext(),
                        AlertDialogFactory.Type.USER_SERVICE_ERROR,
                        state.error
                    ) { dismiss() }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        validateInputFields()
    }

    private fun extractInputFields(): InputFields {
        binding.apply {
            val name = editTextTextPersonName.text.toString()
            val email = editTextTextEmailAddress.text.toString()
            return InputFields(name, email)
        }
    }

    private fun validateInputFields() {
        val inputFields = extractInputFields()
        viewModel.validateInputFields(inputFields.name, inputFields.email)
    }

    private fun enableAdd(enable: Boolean) {
        binding.buttonAdd.isEnabled = enable
    }

    data class InputFields(val name: String, val email: String)
}