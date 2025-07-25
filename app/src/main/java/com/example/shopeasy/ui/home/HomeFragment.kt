package com.example.shopeasy.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
    private val allProducts = mutableListOf<Product>()

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

        // Handle status bar icon visibility
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        insetsController.isAppearanceLightStatusBars = !isDarkTheme

        // Setup RecyclerView
        productAdapter = ProductAdapter(emptyList()) { clickedProduct ->
            val detailFragment = ProductDetailFragment.newInstance(clickedProduct)
            parentFragmentManager.beginTransaction()
                .replace(id, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }

        // Make the entire search bar clickable
        setupSearchView()

        // Observe product list
        productViewModel.productsLive.observe(viewLifecycleOwner) { productEntities ->
            val products = productEntities.map {
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
            productAdapter.submitList(allProducts)
        }

        // Load products
        viewLifecycleOwner.lifecycleScope.launch {
            productViewModel.refreshProducts()
        }
    }

    private fun setupSearchView() {
        // Make the whole search bar card clickable
        binding.searchBarCard.setOnClickListener {
            binding.searchView.requestFocus()
            showKeyboard(binding.searchView)
        }

        // Configure the SearchView
        binding.searchView.apply {
            setIconifiedByDefault(false)  // Method call instead of property
            isFocusable = true
            isClickable = true

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filtered = allProducts.filter {
                        it.name.contains(newText.orEmpty(), ignoreCase = true)
                    }
                    productAdapter.submitList(filtered)

                    // Only show "no matches" when there's a search term and no results
                    binding.noMatchesText.visibility =
                        if (filtered.isEmpty() && !newText.isNullOrEmpty())
                            View.VISIBLE
                        else
                            View.GONE

                    return true
                }
            })
        }
    }

    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}