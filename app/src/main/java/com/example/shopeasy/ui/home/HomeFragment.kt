package com.example.shopeasy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopeasy.databinding.FragmentHomeBinding
import com.example.shopeasy.model.Product
import com.example.shopeasy.ui.detail.ProductDetailFragment
import com.example.shopeasy.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val productViewModel by viewModels<ProductViewModel>()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter = ProductAdapter(emptyList()) { clickedProduct ->
            val detailFragment = ProductDetailFragment.newInstance(clickedProduct)
            parentFragmentManager.beginTransaction()
                .replace(id, detailFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerView.adapter = productAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Button to add 10 sample products
        binding.addSampleProductsButton.setOnClickListener {
            val sampleProducts = List(10) { i ->
                Product(
                    id = i + 1,
                    name = "Product ${i + 1}",
                    price = (100..500).random().toDouble(),
                    description = "Description for product ${i + 1}",
                    imageUrl = ""
                )
            }
            sampleProducts.forEach { product ->
                viewLifecycleOwner.lifecycleScope.launch {
                    productViewModel.addProduct(product) { }
                }
            }
        }

        productViewModel.productsLive.observe(viewLifecycleOwner) { productEntities ->
            val products = productEntities.map {
                Log.d("HomeFragmentId", "Product ID: ${it.id} - Name: ${it.name}")
                Product(

                    id = it.id.toInt()?: 0,
                    name = it.name,
                    price = it.price,
                    description = it.description,
                    imageUrl = it.imageUrl
                )
            }
            productAdapter.submitList(products)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.refreshProducts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}