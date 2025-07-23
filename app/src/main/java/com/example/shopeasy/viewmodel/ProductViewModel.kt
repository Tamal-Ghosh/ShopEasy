package com.example.shopeasy.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.shopeasy.data.repository.ProductRepository
import com.example.shopeasy.model.Product
import com.example.shopeasy.data.model.local.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductRepository(application.applicationContext)

    // LiveData from Room database (local cache)
    val productsLive: LiveData<List<ProductEntity>> = repository.getProductsLive()

    // Optionally, trigger Firestore sync on init
    init {
        viewModelScope.launch {
            repository.refreshProductsFromFirestore()
        }
    }

    fun addProduct(product: Product, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.addProduct(product)
            onComplete(result)
            // Optionally, refresh local cache after adding
            repository.refreshProductsFromFirestore()
        }
    }
    suspend fun refreshProducts() {
        repository.refreshProductsFromFirestore()
    }
}