package com.example.shopeasy.ui.profile

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.shopeasy.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.shopeasy.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fix for transparent status bar overlap + correct icon color
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        insetsController.isAppearanceLightStatusBars = !isDarkTheme // dark icons for light theme

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        // Set user info
        binding.tvUserName.text = user?.displayName ?: "No Name"
        binding.tvUserEmail.text = user?.email ?: "No Email"

        // Show loading indicator
        binding.phoneProgressBar.visibility = View.VISIBLE
        binding.tvUserPhone.text = "Loading..."

        user?.uid?.let { uid ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    val phone = document.getString("phone") ?: "No Phone"
                    binding.tvUserPhone.text = phone
                    binding.phoneProgressBar.visibility = View.GONE
                }
                .addOnFailureListener {
                    binding.tvUserPhone.text = "No Phone"
                    binding.phoneProgressBar.visibility = View.GONE
                }
        } ?: run {
            binding.tvUserPhone.text = "No Phone"
            binding.phoneProgressBar.visibility = View.GONE
        }

        binding.btnChangePassword.setOnClickListener {
            navigateToChangePassword()
        }

        binding.btnAccountSettings.setOnClickListener {
            // TODO: Navigate to Account Settings screen
        }

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun navigateToChangePassword() {
        val changePasswordFragment = ChangePasswordFragment()
        parentFragmentManager.beginTransaction()
            .replace(id, changePasswordFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}