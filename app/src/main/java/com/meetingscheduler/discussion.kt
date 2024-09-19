package com.meetingscheduler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.meetingscheduler.Model.D_Message
import com.meetingscheduler.adapters.DiscutionAdapter


class discussion : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null
    private lateinit var back: ImageView
    lateinit var fabSendMessage: FloatingActionButton
    lateinit var editMessage: EditText
    lateinit var rvChatList: RecyclerView
    lateinit var discutionAdapter: DiscutionAdapter
    lateinit var discussionName: TextView
    lateinit var discussionImage: ImageView
    private var messages = mutableListOf<D_Message>()
    private var listenerRegistration: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = auth.currentUser

        back = findViewById(R.id.back)
        fabSendMessage = findViewById(R.id.fabSendMessage)
        editMessage = findViewById(R.id.editMessage)
        rvChatList = findViewById(R.id.rvChatList)
        discussionImage = findViewById(R.id.imageDiscussion)
        discussionName = findViewById(R.id.nameFriend)

        discutionAdapter = DiscutionAdapter(currentUser!!.uid)
        rvChatList.layoutManager = LinearLayoutManager(this)
        rvChatList.adapter = discutionAdapter

        back.setOnClickListener {
            finish()
        }
        // Ajouter un Snapshot Listener pour écouter les nouvelles données
        listenerRegistration = db.collection("Disscussion").document("discussionId").collection("Messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("TAG", "Erreur de chargement des messages", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    updateMessages(snapshot)
                }
            }
        fabSendMessage.setOnClickListener {
            val message = editMessage.text.toString()
            if (message.isNotEmpty()) {
                val data = hashMapOf(
                    "sender" to currentUser!!.uid,
                    "texte" to message,
                    "timestamp" to Timestamp.now()
                )
                db.collection("Disscussion").document("discussionId").collection("Messages").add(data)
                    .addOnSuccessListener {
                        editMessage.setText("")
                    }.addOnFailureListener {
                        Log.e("TAG", "Erreur lors de l'envoi du message", it)
                    }
            }
        }
    }

    private fun updateMessages(snapshot: QuerySnapshot) {
        messages = snapshot.map { document ->
            D_Message(
                sender = document.getString("sender").toString(),
                texte = document.getString("texte").toString(),
                timestamp = document.getDate("timestamp")!!.time
            )
        }.sortedBy { it.timestamp }.toMutableList()

        discutionAdapter.items = messages
        discutionAdapter.notifyDataSetChanged()
        rvChatList.scrollToPosition(messages.size - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Supprimer le listener pour éviter les fuites de mémoire
        listenerRegistration?.remove()
    }
}