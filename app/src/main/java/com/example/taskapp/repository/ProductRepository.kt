package com.example.taskapp.repository

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskapp.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

private const val PRODUCT_REFERENCE = "products"

class ProductRepository : BaseRepository() {

    private fun getCurrentTime() =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private fun createProduct(
        key: String,
        name: String,
        description: String,
        price: String,
        category: String,
        image: String
    ): Product {
        val timestamp = getCurrentTime()
        return Product(key, name, description, price, category, image, timestamp)
    }

    fun addProduct(
        name: String,
        description: String,
        price: String,
        category: String,
        image: String,
        onSuccessAction: () -> Unit,
        onFailureAction: () -> Unit
    ) {
        val productReference = firebaseDatabase.getReference(PRODUCT_REFERENCE)
        val key = productReference.push().key ?: ""
        val post = createProduct(key, name, description, price, category, image)
        val fileName = imageNameFormatter()
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$fileName")
        storageReference.putFile(post.image.toUri())
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener { url ->
                    val imgLink = url.result.toString()
                    post.image = imgLink
                    productReference.child(key)
                        .setValue(post)
                        .addOnSuccessListener { onSuccessAction() }
                        .addOnFailureListener { onFailureAction() }
                }
            }
    }

    private lateinit var productsValueEventListener: ValueEventListener
    private val productValues = MutableLiveData<List<Product>>()

    private fun listenForProductsValueChanges() {
        productsValueEventListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val posts = dataSnapshot.children.mapNotNull {
                        it.getValue(Product::class.java)
                    }.toList()
                    productValues.postValue(posts)
                } else {
                    productValues.postValue(emptyList())
                }
            }
        }
        firebaseDatabase.getReference(PRODUCT_REFERENCE)
            .addValueEventListener(productsValueEventListener)
    }

    fun onProductsValuesChange(): LiveData<List<Product>> {
        listenForProductsValueChanges()
        return productValues
    }

    fun removeProductsValuesChangesListener() {
        firebaseDatabase.getReference(PRODUCT_REFERENCE)
            .removeEventListener(productsValueEventListener)
    }

    private fun imageNameFormatter(): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        return formatter.format(now)
    }

    fun updateProduct(key: String, product: Product) {
        firebaseDatabase.getReference(PRODUCT_REFERENCE)
            .child(key)
            .setValue(product)
    }

    fun deleteProduct(key: String) {
        firebaseDatabase.getReference(PRODUCT_REFERENCE)
            .child(key)
            .removeValue()
    }

}