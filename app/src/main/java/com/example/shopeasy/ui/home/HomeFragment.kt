package com.example.shopeasy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopeasy.databinding.FragmentHomeBinding
import com.example.shopeasy.model.Product
import com.example.shopeasy.ui.detail.ProductDetailFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyProducts = listOf(
            Product(1, "Item 1", 100.0, "Description 1", "image_url_1"),
            Product(2, "Item 2", 200.0, "Description 2", "image_url_2"),
            // Add more products if you want
        )

        productAdapter = ProductAdapter(dummyProducts) { clickedProduct ->
            // Replace with ProductDetailFragment and pass the product
            val detailFragment = ProductDetailFragment.newInstance(clickedProduct)
            parentFragmentManager.beginTransaction()
                .replace(id, detailFragment)  // 'id' is container ID of HomeFragment
                .addToBackStack(null)         // Add to back stack to allow back navigation
                .commit()
        }

        binding.recyclerView.adapter = productAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()) // or GridLayoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
