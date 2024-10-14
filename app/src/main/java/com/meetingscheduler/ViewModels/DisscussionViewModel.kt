package com.meetingscheduler.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.Message
import com.meetingscheduler.app.utils.Utils.Companion.getHeur

class DisscussionViewModel : ViewModel() {
    //Intialiser Firebase ,Firestore , Auth
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val currentUser = auth.currentUser
    private var listenerRegistration: ListenerRegistration? = null

    //Live data  pour  la  liste  des  messages
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages
    private var liste_messages = mutableListOf<Message>()

    /**
     * Get Messages  of  this disscussion
     */
    fun getMessages(id: String) {
        listenerRegistration =
            db.collection("Discussion").document(id).collection("messages").orderBy("timestamp")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d("Discussion", "Erreur de chargement des messages")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        updateMessages(snapshot)
                    }
                }
    }

    /**
     * Update Messages  of  this disscussion
     */
    private fun updateMessages(snapshot: QuerySnapshot) {
        liste_messages = snapshot.map { doc ->
            Message(
                doc.id,
                doc.getString("sender") ?: "",
                doc.getString("texte").toString(),
                getHeur(doc.getDate("timestamp")!!)
            )
        }.toMutableList()
        _messages.value = liste_messages

    }

    /**
     * Add Message to this disscussion
     */
    fun addMessage(id: String, texte: String) {
        val message = hashMapOf<String, Any>(
            "sender" to currentUser!!.uid,
            "texte" to texte,
            "timestamp" to Timestamp.now()
        )
        db.collection("Discussion").document(id).collection("messages").add(message)
            .addOnSuccessListener {

            }.addOnFailureListener {
                Log.d("Discussion", "Echec  d'envoi de ce message")
            }
    }

    /**
     * Delete Message to this disscussion
     */
    fun supprimerMessage(idDisscussion: String, idMessage: String) {
        db.collection("Discussion").document(idDisscussion).collection("messages")
            .document(idMessage).delete().addOnSuccessListener {

            }.addOnFailureListener {
                Log.d("Discussion", "Echec  de suppression de ce message")
            }

    }

    override fun onCleared() {
        listenerRegistration?.remove()
    }
}