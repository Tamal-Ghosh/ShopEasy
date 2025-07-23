package com.example.shopeasy.ui.detail

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
import com.example.shopeasy.R
import com.example.shopeasy.databinding.FragmentProductDetailBinding
import com.example.shopeasy.model.Product
import com.example.shopeasy.ui.cart.CartFragment
import com.example.shopeasy.viewmodel.CartViewModel

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fix for transparent status bar overlap + correct icon color
        val window = requireActivity().window
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        val product = arguments?.getParcelable<Product>("product")

        product?.let { prod->
            binding.productName.text = prod.name
            binding.productPrice.text = "৳${prod.price}"
            binding.productDescription.text = prod.description
            // TODO: Load image into binding.productImage if you have image loading set up

            binding.addToCartButton.setOnClickListener {
                cartViewModel.addToCart(prod)

                Toast.makeText(requireContext(), "${prod.name} added to cart", Toast.LENGTH_SHORT).show()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CartFragment())
                    .addToBackStack(null) // so user can navigate back
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(product: Product): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable("product", product)
            fragment.arguments = bundle
            return fragment
        }
    }
}
