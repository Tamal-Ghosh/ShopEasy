package com.example.shopeasy.ui.home


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.WindowCompat
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
    private val allProducts = mutableListOf<Product>() // Placeholder, will be updated by ViewModel

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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        insetsController.isAppearanceLightStatusBars = !isDarkTheme // dark icons for light theme


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

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = allProducts.filter {
                    it.name.contains(newText.orEmpty(), ignoreCase = true)
                }
                productAdapter.submitList(filtered)
                return true
            }
        })

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
            allProducts.clear()
            allProducts.addAll(products)
            productAdapter.submitList(products)
        }

        // Load data from Firestore
        viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.refreshProducts()
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = allProducts.filter {
                    it.name.contains(newText.orEmpty(), ignoreCase = true)
                }
                productAdapter.submitList(filtered)
                binding.noMatchesText.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
