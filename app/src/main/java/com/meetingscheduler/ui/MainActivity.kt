package com.meetingscheduler.ui

import Home
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.meetingscheduler.Chat
import com.meetingscheduler.Model.User
import com.meetingscheduler.Notifications
import com.meetingscheduler.R
import com.meetingscheduler.Settings

class MainActivity : AppCompatActivity() {

    // Fonction appelée lors de la création de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des fragments en fonction du type d'utilisateur
        val user: User? = intent.getParcelableExtra("user") as? User
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        replaceFragment(Home())
        user?.let { user ->
            // Remplace le fragment par défaut en fonction du type d'utilisateur
            val initialFragment = if (user.type_user == "etudiant") Home() else Home()
            replaceFragment(initialFragment)

            // Configuration du listener pour la navigation
            bottomNav.setOnItemSelectedListener { item ->
                // Détermine quel fragment afficher en fonction de l'élément sélectionné
                val fragment = when (item.itemId) {
                    R.id.item_home -> initialFragment
                    R.id.item_chat -> Chat()
                    R.id.item_notification -> Notifications()
                    R.id.item_settings -> Settings()
                    else -> null
                }
                // Remplace le fragment seulement si un fragment valide a été déterminé
                fragment?.let { replaceFragment(it) }
                true
            }
        }
    }

    // Fonction pour remplacer le fragment affiché dans le conteneur
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragm, fragment)
            .commit()
    }
}
