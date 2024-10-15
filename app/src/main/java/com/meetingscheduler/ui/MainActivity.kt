package com.meetingscheduler.ui

import Home
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.meetingscheduler.R

class MainActivity : AppCompatActivity() {

    // Fonction appelée lors de la création de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des fragments en fonction du type d'utilisateur
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val fabBtn = findViewById<FloatingActionButton>(R.id.addFabBtn)
        replaceFragment(Home())

        // Configuration du listener pour la navigation
        bottomNav.setOnItemSelectedListener { item ->
            // Détermine quel fragment afficher en fonction de l'élément sélectionné
            val fragment = when (item.itemId) {
                R.id.item_home -> Home()
                R.id.item_chat -> Chat()
                R.id.item_notification -> Notification()
                R.id.item_settings -> Settings()
                else -> null
            }
            // Remplace le fragment seulement si un fragment valide a été déterminé
            fragment?.let { replaceFragment(it) }
            true
        }
        fabBtn.setOnClickListener {
            replaceFragment(Forme())
        }
    }

    // Fonction pour remplacer le fragment affiché dans le conteneur
    fun replaceFragment(fragment: Fragment) {
        Log.d("Fr", "replaceFragment")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragm, fragment)
            .commit()
    }
}
