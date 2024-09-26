package com.meetingscheduler.ViewModels

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.Notification

class NotificationViewModel : ViewModel() {
    // Liste des notifications
    private val notifications = mutableListOf<Notification>()

    //Live data  pour les notification
    val notification: MutableLiveData<List<Notification>> get() = _notification
    private val _notification = MutableLiveData<List<Notification>>()

    //Intialisation de  firebase et Firestore , Authentification
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser

    fun getNotifications() {
        db.collection("Notifications").whereEqualTo("sender_id", currentUser!!.uid).get()
            .addOnSuccessListener { docs ->
                val notification = Notification("", "", "", "", "", "", "")
                for (doc in docs) {
                    notification.id_notification = doc.id
                    notification.titre_notification = doc.getString("titre") ?: ""
                    notification.description_notification = doc.getString("description") ?: ""
                    notification.date_notification = doc.getString("date") ?: ""
                    notification.image_notification = doc.getString("image") ?: ""
                    notification.type_notification = doc.getString("type") ?: ""
                    notification.sender_id = doc.getString("sender_id") ?: ""
                    notifications.add(notification)
                }
                _notification.value = notifications
            }

    }
}