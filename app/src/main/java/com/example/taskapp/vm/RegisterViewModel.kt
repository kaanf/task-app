package com.example.taskapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskapp.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private var userMutableLiveData: MutableLiveData<FirebaseUser> = firebaseRepository.getUserMutableLiveData()

    fun registerUser(email: String, password: String) {
        firebaseRepository.registerUser(email, password)
    }

    fun getMutableLiveData(): MutableLiveData<FirebaseUser>  {
        return userMutableLiveData
    }

}