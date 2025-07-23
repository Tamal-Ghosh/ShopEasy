package com.example.shopeasy.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
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

        // Adjust status bar icon color based on light/dark theme
        val window = requireActivity().window
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        // RecyclerView setup
        productAdapter = ProductAdapter(emptyList()) { clickedProduct ->
            val detailFragment = ProductDetailFragment.newInstance(clickedProduct)
            parentFragmentManager.beginTransaction()
                .replace(id, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Observe products from ViewModel
        productViewModel.productsLive.observe(viewLifecycleOwner) { productEntities ->
            val products = productEntities.map {
                Log.d("HomeFragmentId", "Product ID: ${it.id} - Name: ${it.name}")
                Product(
                    id = it.id.toInt() ?: 0,
                    name = it.name,
                    price = it.price,
                    description = it.description,
                    imageUrl = it.imageUrl
                )
            }
            productAdapter.submitList(products)
        }

        // Load data from Firestore
        viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.refreshProducts()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
