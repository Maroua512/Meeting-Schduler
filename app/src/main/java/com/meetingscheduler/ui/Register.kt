package com.meetingscheduler.ui

// Importation des ViewModel et utilitaires nécessaires pour l'Activity Register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.meetingscheduler.Utils.ConfirmPasswordValidator
import com.meetingscheduler.Utils.EmailValidation
import com.meetingscheduler.Utils.PasswordValidation
import com.meetingscheduler.ValidateService
import com.meetingscheduler.ViewModels.RegisterViewModel
import com.meetingscheduler.databinding.ActivityRegisterBinding


// Classe Register qui représente l'Activity pour l'enregistrement des utilisateurs
class Register : AppCompatActivity() {

    // Liaison avec les éléments de l'interface utilisateur définis dans le fichier XML `activity_register.xml`
    private lateinit var binding: ActivityRegisterBinding

    // Création d'une instance du ViewModel `RegisterViewModel` pour gérer les données et la logique d'affaires
    private val viewModel: RegisterViewModel by viewModels()

    // Fonction appelée lors de la création de l'Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Liaison de la vue avec le fichier de mise en page via `ActivityRegisterBinding`
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gestion du clic sur le bouton d'inscription
        binding.btnSignUp.setOnClickListener {
            // Récupération des champs de texte pour l'email, le mot de passe, et la confirmation du mot de passe
            val email = binding.email
            val password = binding.password
            val confirmPassword = binding.ConfirmPassword

            // Validation des champs (email, mot de passe, confirmation du mot de passe)
            if (validateFields(email, password, confirmPassword)) {
                // Si la validation réussit, on vérifie si l'utilisateur existe déjà via le ViewModel
                viewModel.checkIfUserExists(email.editText?.text.toString())
            }
        }
        // Appel de la fonction qui observe les données du ViewModel
        observeViewModel()
    }

    // Fonction pour valider les champs de saisie
    private fun validateFields(
        email: TextInputLayout,
        password: TextInputLayout,
        confirmPassword: TextInputLayout
    ): Boolean {
        // Création d'un service de validation avec les règles spécifiques pour chaque champ (Email, Mot de passe, Confirmation)
        val validation = ValidateService(
            listOf(
                email to EmailValidation(), // Validation de l'email
                password to PasswordValidation(binding.formatPassword), // Validation du mot de passe avec un format spécifique
                confirmPassword to ConfirmPasswordValidator(password) // Validation de la confirmation du mot de passe
            )
        )

        // Retourne true si toutes les validations sont correctes, sinon false
        return validation.validete()
    }

    // Fonction pour observer les changements dans le ViewModel
    private fun observeViewModel() {
        // Observer l'état de chargement (`isLoading`), affiche ou masque la ProgressBar
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observer si l'utilisateur existe déjà (`userExists`)
        viewModel.userExists.observe(this, Observer { exists ->
            if (exists) {
                // Si l'utilisateur existe, on récupère l'email et le mot de passe et on crée l'utilisateur
                val email = binding.email.editText?.text.toString()
                val password = binding.password.editText?.text.toString()
                viewModel.createUser(email, password)
            } else {
                // Sinon, afficher un message indiquant que l'utilisateur n'existe pas
                Toast.makeText(
                    this,
                    "L'utilisateur n'existe pas dans la base de données.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        // Observer si l'email a été vérifié (`emailVerified`)
        viewModel.emailVerified.observe(this, Observer { isVerified ->
            if (isVerified) {
                // Affiche un message si l'email a été vérifié avec succès
                Toast.makeText(this, "Email vérifié avec succès", Toast.LENGTH_SHORT).show()
                // Assurez-vous que `foundUser` n'est pas null avant d'accéder à ses propriétés
                viewModel.founderUser.value?.let { user ->
                    // Démarre l'activité pour les étudiants
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("user", user)
                    }
                    startActivity(intent)
                } ?: run {
                    // Gérer le cas où `foundUser` est null (si nécessaire)
                    Toast.makeText(this, "Utilisateur non trouvé.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Observer si la vérification a expiré (`verificationTimeout`)
        viewModel.verificationTimeout.observe(this, Observer { timeout ->
            if (timeout) {
                // Affiche un message d'erreur si le délai de vérification est dépassé
                Toast.makeText(this, "Échec de la vérification, délai dépassé.", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        // Observer les messages d'erreur (`errorMessage`)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                // Affiche le message d'erreur s'il existe
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
