package com.example.shopeasy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shopeasy.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnChangePassword.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val currentPassword = binding.etCurrentPassword.text.toString().trim()
        val newPassword = binding.etNewPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        // Validation
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill all fields")
            return
        }

        if (newPassword.length < 6) {
            showToast("New password must be at least 6 characters")
            return
        }

        if (newPassword != confirmPassword) {
            showToast("Passwords don't match")
            return
        }

        // Show loading indicator
        binding.progressBar.visibility = View.VISIBLE
        binding.btnChangePassword.isEnabled = false

        // Get current user
        val user = auth.currentUser
        if (user != null && user.email != null) {
            // Re-authenticate user before changing password
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    // Authentication successful, now change password
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            showToast("Password updated successfully")
                            parentFragmentManager.popBackStack()
                        }
                        .addOnFailureListener { e ->
                            showToast("Failed to update password: ${e.message}")
                            resetLoadingState()
                        }
                }
                .addOnFailureListener { e ->
                    showToast("Current password is incorrect")
                    resetLoadingState()
                }
        } else {
            showToast("User not authenticated")
            resetLoadingState()
        }
    }

    private fun resetLoadingState() {
        binding.progressBar.visibility = View.GONE
        binding.btnChangePassword.isEnabled = true
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}