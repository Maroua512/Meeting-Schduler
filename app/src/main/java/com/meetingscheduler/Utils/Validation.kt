package com.meetingscheduler.Utils

import android.graphics.Color
import android.util.Log
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.meetingscheduler.Interfaces.ValidationResult
import com.meetingscheduler.Interfaces.Validator
import com.meetingscheduler.R


//Validation d'un email saisie
class EmailValidation : Validator {
    override fun validate(input: String): ValidationResult {
        return if (input.isEmpty()) {
            ValidationResult(false, "Email is required !")
        } else if (!isValidEmail(input)) {
            ValidationResult(false, "Invalid email format !")
        } else {
            ValidationResult(true)
        }
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}

//Validation d'un Password saisie
class PasswordValidation : Validator {
    override fun validate(input: String): ValidationResult {
        return if (input.isEmpty()) {
            ValidationResult(false, "Password  is  required  !")
        }  else {
            ValidationResult(true)
        }
    }

}

// Validation de password
class ConfirmPasswordValidator(private val password: TextInputLayout,private val formatPassword: TextView) : Validator {
    override fun validate(input: String): ValidationResult {
        return when {
            input.isEmpty() -> {
                ValidationResult(false, "Confirm password is required !")
            }

            input != password.editText?.text.toString() -> {
                ValidationResult(false, "Passwords do not match!")
            }
           !isCorrectPassword(input)-> {
                formatPassword.setTextColor(Color.RED)
                ValidationResult(false, "Invalid  password format ! ")
            }

            else -> {
                ValidationResult(true)
            }


        }
    }
    fun isCorrectPassword(password: String): Boolean {
        val passwordPattern ="^(?=.*[a-z].*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d.*\\d)(?=.*[@\$!%*?&].*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{12,}\$"
           Log.d("PasswordValidation", "Password: ${password.matches(passwordPattern.toRegex())}")
        return password.matches(passwordPattern.toRegex())
    }
}