package com.example.shopeasy.ui.cart

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopeasy.databinding.FragmentCartBinding
import com.example.shopeasy.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fix for transparent status bar overlap + correct icon color
        val window = requireActivity().window
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        cartAdapter = CartAdapter(
            emptyList(),
            onRemoveClick = { productToRemove ->
                cartViewModel.removeFromCart(productToRemove)
                Toast.makeText(requireContext(), "${productToRemove.name} removed.", Toast.LENGTH_SHORT).show()
            },
            onQuantityChange = { productId, increase ->
                cartViewModel.changeQuantity(productId, increase)
                // Optionally update total price here if needed
            }
        )

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = cartAdapter

        cartViewModel.cartItems.observe(viewLifecycleOwner) { products ->
            cartAdapter.updateData(products)
            val total = cartViewModel.getTotalPrice()
            binding.totalPriceTextView.text = "Total: ৳$total"
        }

        binding.checkoutButton.setOnClickListener {
            Toast.makeText(requireContext(), "Checkout clicked", Toast.LENGTH_SHORT).show()
            // TODO: Implement checkout logic
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}