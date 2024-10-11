package com.meetingscheduler.app.utils

import android.app.Activity
import android.app.AlertDialog

import android.content.Context

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.meetingscheduler.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class Utils {

    companion object {

        // Afficher un toast
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        // Afficher une boîte de dialogue d'alerte
        fun showAlertDialog(activity: Activity, title: String, message: String) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        // Afficher une ProgressBar dans une AlertDialog
        fun showProgressBar(activity: Activity, message: String): AlertDialog {
            val builder = AlertDialog.Builder(activity)
            val inflater = activity.layoutInflater
            val dialogView = inflater.inflate(R.layout.progress_dialog, null)
            builder.setView(dialogView)
            builder.setMessage(message)
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.show()
            return dialog
        }

        // Fermer l'AlertDialog avec ProgressBar
        fun hideProgressBar(dialog: AlertDialog) {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }

        // Ajouter d'autres fonctions ici selon vos besoins
        fun isValidEmail(email: String): Boolean {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            return email.matches(emailPattern.toRegex())
        }

        fun isCorrectPassword(password: String): Boolean {
            val passwordPattern =
                "^(?=.*[a-z].*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d.*\\d)(?=.*[@\$!%*?&].*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{12,}\$\n"
            return password.matches(passwordPattern.toRegex())
        }

        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            val regex =
                Regex("(05|06|07)\\d{8}") // Expression régulière pour vérifier le format du numéro
            return phoneNumber.matches(regex)
        }

        fun sendVerificationEmail(user: FirebaseUser) {
            user.sendEmailVerification().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("Email de vérification envoyé. Veuillez vérifier votre email.")
                    } else {
                        println("Erreur lors de l'envoi de l'email de vérification.")
                    }
                }
        }
        fun uploadPhoto(context: Context, photoUri: String,id:String) {
            // Implémentez la logique d'envoi de la photo ici

        }
        // Fonction pour convertir la date en chaîne de caractères
        fun getJour(date: Date): String {
            return SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
        }

        fun getHeur(date: Date): String {
            return SimpleDateFormat("HH:MM", Locale.getDefault()).format(date)

        }
    }

}
