package com.example.taskapp.repository

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskapp.model.Product
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

private const val PRODUCT_REFERENCE = "products"

class ProductRepository : BaseRepository() {

    private fun getCurrentTime() =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val databaseReference = firebaseDatabase.getReference(PRODUCT_REFERENCE)
    private lateinit var productsValueEventListener: ValueEventListener
    private val productValues = MutableLiveData<List<Product>>()

    fun addProduct(product: Product, onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
        val productReference = firebaseDatabase.getReference(PRODUCT_REFERENCE)
        product.key = productReference.push().key ?: ""
        product.timestamp = getCurrentTime()
        val storageReference = firebaseStorage.reference.child("images/${imageNameFormatter()}")
        storageReference.putFile(product.image.toUri())
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener { url ->
                    val imgLink = url.result.toString()
                    product.image = imgLink
                    productReference.child(product.key)
                        .setValue(product)
                        .addOnSuccessListener { onSuccessAction() }
                        .addOnFailureListener { onFailureAction() }
                }
            }
    }

    private fun listenForProductsValueChanges() {
        productsValueEventListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val products = dataSnapshot.children.mapNotNull {
                        it.getValue(Product::class.java)
                    }.toList()
                    productValues.postValue(products)
                } else productValues.postValue(emptyList())
            }
        }
        databaseReference.addValueEventListener(productsValueEventListener)
    }

    fun onProductsValuesChange(): LiveData<List<Product>> {
        listenForProductsValueChanges()
        return productValues
    }

    fun removeProductsValuesChangesListener() {
        databaseReference.removeEventListener(productsValueEventListener)
    }

    private fun imageNameFormatter(): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        return formatter.format(now)
    }

    fun updateProduct(key: String, product: Product) {
        databaseReference.child(key).setValue(product)
    }

    fun deleteProduct(key: String) {
        databaseReference.child(key).removeValue()
    }

}