package com.example.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentRegisterBinding
import com.example.taskapp.repository.FirebaseRepository
import com.example.taskapp.ui.base.BaseFragment
import com.example.taskapp.utils.Extensions
import com.example.taskapp.utils.Validation
import com.example.taskapp.vm.RegisterViewModel

class RegisterFragment :
    BaseFragment<RegisterViewModel, FragmentRegisterBinding, FirebaseRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMutableLiveData().observe(viewLifecycleOwner, {
            when (it) {
                null -> Extensions.showToast(requireContext(), "Failure")
                else -> findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        })
        binding.apply {
            icBackArrow.setOnClickListener {
                findNavController().navigate(
                    R.id.loginFragment
                )
            }
            registerButton.setOnClickListener {
                val email = registerEmail.text.toString()
                val password = registerPassword.text.toString()
                val confPassword = registerConfPassword.text.toString()
                if (Validation.validateData(requireContext(), email, password, confPassword)) {
                    viewModel.registerUser(email, password)
                }
            }
            registerLoginButton.setOnClickListener {
                findNavController().popBackStack(
                    R.id.loginFragment,
                    false
                )
            }
        }
    }

    override fun getViewModel(): Class<RegisterViewModel> = RegisterViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = FirebaseRepository()

}