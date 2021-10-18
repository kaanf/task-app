package com.example.taskapp.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.taskapp.model.Product
import com.example.taskapp.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

}