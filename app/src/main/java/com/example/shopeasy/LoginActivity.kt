package com.example.shopeasy

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.shopeasy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var fauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Status bar appearance for light/dark theme
        val window = window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        insetsController.isAppearanceLightStatusBars = !isDarkTheme // dark icons for light theme

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

            // Sign in with Firebase Authentication
            fauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = fauth.currentUser
                        // Check if email is verified
                        if (user != null && user.isEmailVerified) {
                            Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // Email not verified
                            Toast.makeText(
                                this,
                                "Please verify your email before logging in.",
                                Toast.LENGTH_LONG
                            ).show()
                            fauth.signOut()
                        }
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