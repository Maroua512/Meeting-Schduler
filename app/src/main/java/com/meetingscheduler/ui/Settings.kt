package com.meetingscheduler.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.meetingscheduler.R


class Settings : Fragment() {
    lateinit var btnlogout :TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val view = inflater.inflate(R.layout.fragment_settings, container, false)
        btnlogout = view.findViewById(R.id.btnLogout)

        btnlogout.setOnClickListener {
            showDialogLogout()
        }
        return  view
    }
    private  fun showDialogLogout(){
        val dialog  = Dialog(requireContext())
        dialog.setContentView(R.layout.activity_message_logout)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)

        val cancelButton = dialog.findViewById<AppCompatButton>(R.id.btnNo)
        val confirmButton = dialog.findViewById<AppCompatButton>(R.id.btnYes)

        cancelButton.setOnClickListener {
            // Action à effectuer lorsque l'utilisateur clique sur le bouton d'annulation
            dialog.dismiss() // Fermer la boîte de dialogue
        }

        // Définir un écouteur de clic sur le bouton de confirmation
        confirmButton.setOnClickListener {
            // Action à effectuer lorsque l'utilisateur clique sur le bouton de confirmation
            val auth = Firebase.auth
            auth.signOut()
            Intent(requireActivity(), Authentification::class.java).also {
                startActivity(it)
            }
            requireActivity().finish()
            // Fermer la boîte de dialogue après avoir effectué l'action
            dialog.dismiss()
        }

        dialog.show()
    }

}