package com.meetingscheduler.Utils

import android.graphics.Color
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.meetingscheduler.Interfaces.ValidationResult
import com.meetingscheduler.Interfaces.Validator


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
class PasswordValidation(private val formatText: TextView) : Validator {
    override fun validate(input: String): ValidationResult {
        return if (input.isEmpty()) {
            ValidationResult(false, "Passwrd  is  required  !")
        } else if (isCorrectPassword(input)) {
            formatText.setTextColor(Color.RED)
            ValidationResult(false, "Invalid  password format ! ")
        } else {
            ValidationResult(true)
        }
    }

    fun isCorrectPassword(password: String): Boolean {
        //val passwordPattern ="^(?=.*[a-z].*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d.*\\d)(?=.*[@\$!%*?&].*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{12,}\$\n"
        val passwordPattern =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{12,}$"

        return password.matches(passwordPattern.toRegex())
    }
}

// Validation de password
class ConfirmPasswordValidator(private val password: TextInputLayout) : Validator {
    override fun validate(input: String): ValidationResult {
        return when {
            input.isEmpty() -> {
                ValidationResult(false, "Confirm password is required !")
            }

            input != password.editText?.text.toString() -> {
                ValidationResult(false, "Passwords do not match!")
            }

            else -> {
                ValidationResult(true)
            }


        }
    }
}