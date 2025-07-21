package com.example.shopeasy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.shopeasy.databinding.ActivityMainBinding
import com.example.shopeasy.ui.cart.CartFragment
import com.example.shopeasy.ui.home.HomeFragment
import com.example.shopeasy.ui.profile.AccountFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fauth = FirebaseAuth.getInstance()

        // Default fragment
        loadFragment(HomeFragment())

        // Bottom Navigation
        binding.bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home-> HomeFragment()
                R.id.nav_cart -> CartFragment()
                R.id.nav_account -> AccountFragment()
                else -> HomeFragment()
            }
            loadFragment(selectedFragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
