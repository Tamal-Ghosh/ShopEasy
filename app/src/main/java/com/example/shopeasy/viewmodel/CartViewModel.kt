package com.example.shopeasy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopeasy.model.Product

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<MutableList<Product>>(mutableListOf())
    val cartItems: LiveData<MutableList<Product>> = _cartItems

    fun addToCart(product: Product) {
        val currentList = _cartItems.value ?: mutableListOf()
        currentList.add(product)
        _cartItems.value = currentList
    }

    fun removeFromCart(product: Product) {
        val currentList = _cartItems.value ?: mutableListOf()
        currentList.remove(product)
        _cartItems.value = currentList
    }

    fun getTotalPrice(): Double {
        return _cartItems.value?.sumOf { it.price } ?: 0.0
    }
}
