package com.meetingscheduler.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.D_Message

class DisscussionViewModel : ViewModel() {
    //Intialiser Firebase ,Firestore , Auth
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val currentUser = auth.currentUser
    private var listenerRegistration : ListenerRegistration? =null

    //Live data  pour  la  liste  des  messages
    private val _messages = MutableLiveData<List<D_Message>>()
    val messages: LiveData<List<D_Message>> = _messages
    private val liste_messages = mutableListOf<D_Message>()

    // fonction pour recuperer les messages
    fun getMessages(id: String) {

    }

    // fonction pour ajouter  un message
    fun addMessage(id: String, message: String) {
        val message = hashMapOf<String, Any>(
            "sender" to currentUser?.email.toString(),
            "texte" to message,
            "timestamp" to System.currentTimeMillis()
        )
        db.collection("Disscussion").document(id).collection("Messages").add(message)
            .addOnSuccessListener {

            }.addOnFailureListener {
            Log.d("Disscussion", "Echec  d'envoi de ce message")
        }
    }

    //fonction pour supprimer un message
    fun supprimerMessage(id_disscussion: String, id_message: String) {
        db.collection("Disscussion").document(id_disscussion).collection("Messages")
            .document(id_message).delete().addOnSuccessListener {

        }.addOnFailureListener {
            Log.d("Disscussion", "Echec  de suppression de ce message")
        }

    }
}