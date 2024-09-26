package com.meetingscheduler.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SettingViewModel:ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser

  // Live data  et  MutableLiveData  pour nom d'utilisateur
    private val _username = MutableLiveData<String>()
    val username: MutableLiveData<String> get() = _username
    // Live data  et  MutableLiveData  pour l'image du profile
    private val _profileImage = MutableLiveData<String>()
    val profileImage: MutableLiveData<String> get() = _profileImage

    // Fonction pour recuperer  le nom d'utilisateur
    fun getUsername() {
        db.collection("Utilisateurs").document(currentUser!!.uid).get().addOnSuccessListener {user->
           if(user.exists()){
               _username.value = user.getString("username").toString()
           }
        }.addOnFailureListener {
            _username.value = ""
        }
    }
  // Fonction pour  recuperer l'image du profile
    fun getImageProfile(newProfileImage: String) {
        db.collection("Utilisateurs").document(currentUser!!.uid).get().addOnSuccessListener { user->
            if(user.exists()){
               _profileImage.value = user.getString("profileImage").toString()
            }
        }.addOnFailureListener {
            Log.e("TAG", "Erreur de recuperation de l'image du profile: ")
        }

    }
    // Fonction pour  mettre à jour l'image du profile
    fun updateImageProfile(newProfileImage: String) {
        db.collection("Utilisateurs").document(currentUser!!.uid).update("profileImage", newProfileImage).addOnSuccessListener {
            _profileImage.value = newProfileImage
        }.addOnFailureListener {
            Log.e("TAG", "Erreur de mise à jour de l'image du profile: ")
        }

    }

}