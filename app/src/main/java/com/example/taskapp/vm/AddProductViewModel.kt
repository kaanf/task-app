package com.example.taskapp.vm

import androidx.lifecycle.ViewModel
import com.example.taskapp.repository.ProductRepository

class AddProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    fun addProduct(
        name: String,
        description: String,
        price: String,
        category: String,
        image: String,
        onSuccessAction: () -> Unit,
        onFailureAction: () -> Unit
    ) {
        productRepository.addProduct(
            name,
            description,
            price,
            category,
            image,
            onSuccessAction,
            onFailureAction
        )
    }
}