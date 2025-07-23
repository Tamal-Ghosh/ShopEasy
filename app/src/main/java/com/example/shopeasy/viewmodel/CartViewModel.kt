package com.example.shopeasy.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopeasy.model.Product

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<Product>>(emptyList())
    val cartItems: LiveData<List<Product>> get() = _cartItems

    fun addToCart(product: Product) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.id == product.id }
        //val index=-1
        Log.d("CartViewModelIndex", "index found: $index" +"id: ${product.id}")

        if (index >= 0) {
            val updatedProduct = currentList[index].copy(quantity = currentList[index].quantity + 1)
            currentList[index] = updatedProduct
        } else {
            currentList.add(product.copy(quantity = 1))
        }
        _cartItems.value = currentList
    }

    fun removeFromCart(product: Product) {
        val currentList = _cartItems.value.orEmpty().toMutableList()
        currentList.removeAll { it.id == product.id }
        _cartItems.value = currentList
    }

    fun changeQuantity(productId: Int, increase: Boolean) {
        val updatedList = _cartItems.value.orEmpty().map {
            if (it.id == productId) {
                val newQty = if (increase) it.quantity + 1 else maxOf(1, it.quantity - 1)
                it.copy(quantity = newQty)
            } else it
        }
        _cartItems.value = updatedList
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.orEmpty().sumOf { it.price * it.quantity }
    }
}

