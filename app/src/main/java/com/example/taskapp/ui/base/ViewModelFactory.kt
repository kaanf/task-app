package com.example.taskapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.repository.BaseRepository
import com.example.taskapp.repository.FirebaseRepository
import com.example.taskapp.repository.ProductRepository
import com.example.taskapp.vm.*

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository as FirebaseRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as FirebaseRepository) as T
            modelClass.isAssignableFrom(AddProductViewModel::class.java) -> AddProductViewModel(repository as ProductRepository) as T
            modelClass.isAssignableFrom(ProductViewModel::class.java) -> AddProductViewModel(repository as ProductRepository) as T
            modelClass.isAssignableFrom(ProductDetailsViewModel::class.java) -> ProductDetailsViewModel(repository as ProductRepository) as T
            else -> throw IllegalAccessException("ViewModel class not found.")
        }
    }
}