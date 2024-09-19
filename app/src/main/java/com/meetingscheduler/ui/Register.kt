package com.meetingscheduler.ui
import com.meetingscheduler.ViewModels.RegisterViewModel
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

import com.meetingscheduler.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val email = binding.email
            val password = binding.password
            val confirmPassword = binding.ConfirmPassword

            // Validation des champs
            if (validateFields(email, password, confirmPassword)) {
                viewModel.checkIfUserExists(email.editText?.text.toString())
            }
        }

        observeViewModel()
    }

    private fun validateFields(email: TextInputLayout, password: TextInputLayout, confirmPassword: TextInputLayout): Boolean {
        // Ajouter la logique de validation ici (similaire à l'ancien code)
        val validation = ValidateService(listOf(email to EmailValidation() ,password to PasswordValidation(binding.formatPassword) ,confirmPassword to ConfirmPasswordValidator(password)))
        return validation.validete()

    }

    private fun observeViewModel() {
        // Observer les données du ViewModel
        viewModel.isLoading.observe(this,  { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.userExists.observe(this,Observer { exists ->
            if (exists) {
                val email = binding.email.editText?.text.toString()
                val password = binding.password.editText?.text.toString()
                viewModel.createUser(email, password)
            } else {
                Toast.makeText(this, "L'utilisateur n'existe pas dans la base de données.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.emailVerified.observe(this, Observer { isVerified ->
            if (isVerified) {
                Toast.makeText(this, "Email vérifié avec succès", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.verificationTimeout.observe(this, Observer { timeout ->
            if (timeout) {
                Toast.makeText(this, "Échec de la vérification, délai dépassé.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
