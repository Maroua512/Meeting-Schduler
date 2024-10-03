package com.meetingscheduler.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.User

class UserViewModel:ViewModel() {
   private val db = Firebase.firestore
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get()  = _user

    fun setUser(user: User){
        _user.value = user
    }
   fun getUser(id:String) {
       db.collection("users").document(id).get().addOnSuccessListener { result ->
            if(result.exists()){
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