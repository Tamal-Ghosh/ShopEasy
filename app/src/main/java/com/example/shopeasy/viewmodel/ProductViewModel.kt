package com.example.shopeasy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopeasy.model.Product


class ProductViewModel : ViewModel() {

    // Backing property for encapsulation
    private val _products = MutableLiveData<List<Product>>()

    // Public LiveData to observe
    val products: LiveData<List<Product>>
        get() = _products

    init {
        loadMockProducts()
    }

    private fun loadMockProducts() {
        // create a list of mock Product objects
        val mockList = listOf(
            Product(1, "Product 1", 10.0, "Description 1", "https://via.placeholder.com/150"),
            Product(2, "Product 2", 20.0, "Description 2", "https://via.placeholder.com/150")
            // Add more mock products here
        )
        _products.value = mockList
    }
}
