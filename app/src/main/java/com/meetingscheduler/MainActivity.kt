package com.meetingscheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.meetingscheduler.ui.Home_etd

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(Home_etd())
      val btm_nav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
      btm_nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    replaceFragment(Home_etd())
                    true
                }

                R.id.item_chat -> {
                 replaceFragment(Chat())

                    true
                }
                R.id.item_notification -> {
                    replaceFragment(Notifications())
                    true
                }
                R.id.item_settings ->{
                   replaceFragment(Settings())
                    true
                }

                else -> false
            }
        }
    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragm, fragment)
            .commit()
    }
}