package com.example.shopeasy

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val user= FirebaseAuth.getInstance().currentUser

        if(user!=null){
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
        else{
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
        finish()

    }
}