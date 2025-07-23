package com.example.shopeasy.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.shopeasy.data.model.local.AppDatabase
import com.example.shopeasy.data.model.local.ProductEntity
import com.example.shopeasy.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductRepository(context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val productDao = AppDatabase.getDatabase(context).productDao()

    // Returns LiveData from Room to be observed in UI
    fun getProductsLive(): LiveData<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    // Refresh local cache by fetching fresh data from Firestore
    suspend fun refreshProductsFromFirestore() = withContext(Dispatchers.IO) {
        try {
            val result = firestore.collection("products").get().await()

            val productEntities = result.mapNotNull { doc ->
                doc.toObject(Product::class.java)?.let {
                    Log.d("ProductRepository", "Fetched product: ${it.name} with ID: ${doc.id} and product id: ${it.id}")
                    ProductEntity(
                        id = it.id,
                        name = it.name,
                        price = it.price,
                        description = it.description,
                        imageUrl = it.imageUrl
                    )
                }
            }

            // Update local database
            productDao.clearProducts()
            productDao.insertAll(productEntities)

        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error as needed
        }
    }

    // Add product to Firestore
    suspend fun addProduct(product: Product): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            firestore.collection("products").add(product).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
