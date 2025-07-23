package com.example.shopeasy.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopeasy.databinding.ItemCartBinding
import com.example.shopeasy.model.Product

class CartAdapter(
    private var products: List<Product>,
    private val onRemoveClick: (Product) -> Unit,
    private val onQuantityChange: (productId: Int, increase: Boolean) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]
        val b = holder.binding

        b.cartProductName.text = product.name
        b.cartProductPrice.text = "৳%.2f".format(product.price * product.quantity)
        b.quantityText.text = product.quantity.toString()

        b.increaseQtyButton.setOnClickListener {
            onQuantityChange(product.id, true)
        }

        b.decreaseQtyButton.setOnClickListener {
            onQuantityChange(product.id, false)
        }

        b.removeButton.setOnClickListener {
            onRemoveClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}