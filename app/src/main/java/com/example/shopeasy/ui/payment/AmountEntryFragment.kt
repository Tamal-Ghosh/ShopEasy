package com.example.shopeasy.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.shopeasy.R
import com.example.shopeasy.databinding.FragmentAmountEntryBinding
import com.example.shopeasy.viewmodel.CartViewModel

class AmountEntryFragment : Fragment() {

    private var _binding: FragmentAmountEntryBinding? = null
    private val binding get() = _binding!!
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAmountEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show cart total
        cartViewModel.cartItems.observe(viewLifecycleOwner) {
            // Get cart_total from arguments, fallback to ViewModel if not present
            val cartTotal = arguments?.getDouble("cart_total") ?: cartViewModel.getTotalPrice()
            binding.tvCartTotal.text = "Total: ৳$cartTotal"

        }

        binding.btnSubmitAmount.setOnClickListener {
            val amount = binding.etAmount.text.toString().trim().toDoubleOrNull()
            val total = cartViewModel.getTotalPrice()
            if (amount == null || amount < total) {
                Toast.makeText(requireContext(), "Amount must be at least ৳$total", Toast.LENGTH_SHORT).show()
            } else {
                val otpFragment = OTPVerificationFragment.newInstance("Nagad", amount.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, otpFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}