package com.example.taskapp.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentLoginBinding
import com.example.taskapp.repository.FirebaseRepository
import com.example.taskapp.ui.base.BaseFragment
import com.example.taskapp.utils.*
import com.example.taskapp.vm.LoginViewModel

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, FirebaseRepository>() {

    private val router by lazy { Router() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonClickInitializers()
        viewModel.authenticationState.observe(viewLifecycleOwner, { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    Extensions.showToast(requireContext(), "Account found. Please wait...")
                    Handler(Looper.getMainLooper()).postDelayed({
                        router.startHomeScreen(requireActivity())
                    }, 2000)
                }
                AuthenticationState.INVALID_AUTHENTICATION -> {
                    Extensions.showToast(
                        requireContext(),
                        "False e-mail/password. Try again."
                    )
                }
                else -> {
                    Log.d(TAG, "Network connection error, sorry.")
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private fun buttonClickInitializers() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = loginEmail.text.toString()
                val password = loginPassword.text.toString()
                if (Validation.isEmail(requireContext(), email)
                    && Validation.isPassword(requireContext(), password)
                ) viewModel.loginUser(email, password)
            }
            loginRegisterButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    override fun getViewModel() = LoginViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = FirebaseRepository()
}