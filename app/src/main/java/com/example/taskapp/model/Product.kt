package com.example.taskapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val key: String = "",
    var name: String = "",
    var description: String = "",
    var price: String = "",
    var category: String = "",
    var image: String = "",
    val timestamp: String = ""
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "description" to description,
            "price" to price,
            "category" to category,
            "image" to image,
            "timestamp" to timestamp
        )
    }
}
