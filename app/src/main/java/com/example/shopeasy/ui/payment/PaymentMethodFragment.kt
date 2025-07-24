package com.example.shopeasy.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopeasy.R
import com.example.shopeasy.databinding.FragmentAmountEntryBinding
import com.example.shopeasy.databinding.FragmentPaymentMethodBinding
import com.example.shopeasy.ui.payment.OTPVerificationFragment

// PaymentMethodFragment.kt
class PaymentMethodFragment : Fragment() {

    private var _binding: FragmentPaymentMethodBinding? = null
    private val binding get() = _binding!!

    private var cartTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartTotal = arguments?.getDouble("cart_total") ?: 0.0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bkashCard.setOnClickListener {
            goToAmountEntry("bKash")
        }
        binding.nagadCard.setOnClickListener {
            goToAmountEntry("Nagad")
        }
    }

    private fun goToAmountEntry(method: String) {
        val fragment = AmountEntryFragment().apply {
            arguments = Bundle().apply {
                putString("method", method)
                putDouble("cart_total", cartTotal)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
