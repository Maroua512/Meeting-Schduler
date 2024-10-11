package com.meetingscheduler.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.User

class UserViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    // LiveData pour suivre les changements des attributs utilisateur
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userPhoto = MutableLiveData<String>()
    val userPhoto: LiveData<String> get() = _userPhoto
    fun setUser(user: User) {
        _user.value = user
    }
    // Fonction pour récupérer les attributs de l'utilisateur depuis Firebase
    fun fetchUserAttributes(userId: String) {
        db.collection("Utilisateur").document(userId).get()
            .addOnSuccessListener { document ->
                _userName.value = document.getString("name") ?: "Unknown"
                _userPhoto.value = document.getString("photo") ?: "default_photo_url"
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Erreur lors de la récupération des informations utilisateur", e)
            }
    }
    fun getUser(id: String) {
        db.collection("users").document(id).get().addOnSuccessListener { result ->
            if (result.exists()) {
                user.value!!.id_user = result.id
                user.value!!.type_user = result.getString("type").toString()
                user.value!!.role = result.getString("role").toString()
                user.value!!.nom = result.getString("nom").toString()
                user.value!!.prenom = result.getString("prenom").toString()
                user.value!!.email = result.getString("email").toString()
            }
        }
    }
}