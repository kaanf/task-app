package com.example.taskapp.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.taskapp.repository.FirebaseRepository
import com.example.taskapp.utils.UserLiveData
import com.example.taskapp.utils.AuthenticationState

class LoginViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    fun loginUser(email: String, password: String) {
        firebaseRepository.loginUser(email, password)
    }

    val authenticationState = UserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

}