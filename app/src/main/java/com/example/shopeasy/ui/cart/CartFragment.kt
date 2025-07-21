package com.example.shopeasy.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopeasy.databinding.FragmentCartBinding
import com.example.shopeasy.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    // Use activityViewModels() if shared between fragments/activities
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

        cartAdapter = CartAdapter(emptyList()) { productToRemove ->
            cartViewModel.removeFromCart(productToRemove)
            Toast.makeText(requireContext(), "${productToRemove.name} removed.", Toast.LENGTH_SHORT).show()
        }

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = cartAdapter

        // Observe cart items and update UI
        cartViewModel.cartItems.observe(viewLifecycleOwner) { products ->
            cartAdapter = CartAdapter(products) { productToRemove ->
                cartViewModel.removeFromCart(productToRemove)
                Toast.makeText(requireContext(), "${productToRemove.name} removed.", Toast.LENGTH_SHORT).show()
            }
            binding.cartRecyclerView.adapter = cartAdapter

            // Update total price
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
