package com.example.shopeasy

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopeasy.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var fauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fauth = FirebaseAuth.getInstance()

        // If already logged in, go to main screen
        if (fauth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Redirect to Login
        binding.loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Signup logic
        binding.signupButton.setOnClickListener {
            val name = binding.fullName.text.toString().trim()
            val emailText = binding.email.text.toString().trim()
            val passwordText = binding.password.text.toString().trim()
            val phone = binding.phoneNumber.text.toString().trim()

            if (name.isEmpty()) {
                binding.fullName.error = "Full name is required"
                binding.fullName.requestFocus()
                return@setOnClickListener
            }

            if (emailText.isEmpty()) {
                binding.email.error = "Email is required"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                binding.email.error = "Enter a valid email"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (passwordText.isEmpty()) {
                binding.password.error = "Password is required"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                binding.password.error = "Password must be at least 6 characters"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                binding.phoneNumber.error = "Phone number is required"
                binding.phoneNumber.requestFocus()
                return@setOnClickListener
            }

            // Firebase Authentication
            fauth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
