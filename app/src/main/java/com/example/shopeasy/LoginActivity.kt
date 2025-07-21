package com.example.shopeasy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shopeasy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var fauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fauth = FirebaseAuth.getInstance()

        // If user is already logged in, redirect to MainActivity
        fauth.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Login button click listener
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailEditText.error = "Email is required."
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordEditText.error = "Password is required."
                return@setOnClickListener
            }

            fauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Login failed: Email or password is invalid.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Navigate to Signup screen
        binding.createAccountText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }
    }
}
