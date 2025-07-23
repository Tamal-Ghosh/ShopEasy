package com.example.shopeasy.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val imageUrl: String = "",
    val quantity: Int = 1 // Default quantity is 1
) : Parcelable
