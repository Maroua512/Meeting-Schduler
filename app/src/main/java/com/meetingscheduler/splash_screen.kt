package com.meetingscheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.ui.Authentification

class splash_screen : AppCompatActivity() {
    private val auth:FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
                   if(auth.currentUser!=null){
                       Intent(this, MainActivity::class.java).also{
                           startActivity(it)
                       }
                   }
            else{
                Intent(this, Authentification::class.java).also {
                    startActivity(it)
                }
                   }


                finish()
        },3000)
    }
}