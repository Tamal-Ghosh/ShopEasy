package com.example.shopeasy.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int, // use Firestore document ID here
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String
)
