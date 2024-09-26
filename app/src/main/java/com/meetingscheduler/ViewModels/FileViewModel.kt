package com.meetingscheduler.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.File

class FileViewModel : ViewModel() {

    private val _files = MutableLiveData<List<File>>()
    val files: LiveData<List<File>> get() = _files

    // Intialisation de firebase et firestore
    private val db = Firebase.firestore

    //fonction  pour recuperer les fichiers  d'un evenement
    fun getFiles(id_event: String) {
        db.collection("Event").document(id_event).collection("Files").get()
            .addOnSuccessListener { result ->
                val listFiles = mutableListOf<File>()
                for (doc in result) {
                    val file = File(
                        doc.id,
                        doc.getString("name").toString()?: "Non nom disponible",
                        doc.getString("type").toString()
                    )
                    listFiles.add(file)
                }
                // Mise à jour de la liste après la récupération des fichiers

                println("Files  : ${listFiles.size}")
                _files.value = listFiles // Mise à jour du LiveData
                listFiles.clear()
            }.addOnFailureListener {
                Log.e("FileViewModel", "Erreur de recupereration des fichiers de cette evenement")
            }

    }
}