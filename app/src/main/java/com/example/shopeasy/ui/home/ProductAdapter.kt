package com.example.shopeasy.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopeasy.databinding.ItemProductBinding
import com.example.shopeasy.model.Product

class ProductAdapter(
    private var productList: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productName.text = "${product.name} (${product.id})"
            binding.productPrice.text = "৳${product.price}"
            // Uncomment and use Glide/Coil if you have image URLs:
            // Glide.with(binding.productImage.context).load(product.imageUrl).into(binding.productImage)

            binding.root.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    // Update the list and refresh RecyclerView
    fun submitList(products: List<Product>) {
        productList = products
        notifyDataSetChanged()
    }
}
