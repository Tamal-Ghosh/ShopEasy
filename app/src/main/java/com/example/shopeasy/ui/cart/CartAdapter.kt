package com.example.shopeasy.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopeasy.databinding.ItemCartBinding
import com.example.shopeasy.model.Product

class CartAdapter(
    private val cartItems: List<Product>,
    private val onRemoveClick: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.cartProductName.text = product.name
            binding.cartProductPrice.text = "৳${product.price}"

            binding.removeButton.setOnClickListener {
                onRemoveClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}
