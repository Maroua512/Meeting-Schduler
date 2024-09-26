package com.meetingscheduler.ui

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.D_Message
import com.meetingscheduler.R
import com.meetingscheduler.ViewModels.DisscussionViewModel
import com.meetingscheduler.adapters.DiscutionAdapter


class Discussion : AppCompatActivity() {
    lateinit var discutionViewModel: DisscussionViewModel
    lateinit var discutionAdapter: DiscutionAdapter
    private lateinit var back: ImageView
    lateinit var fabSendMessage: FloatingActionButton
    lateinit var editMessage: EditText
    lateinit var rvChatList: RecyclerView
    lateinit var discussionName: TextView
    lateinit var discussionImage: ImageView
    private var listenerRegistration: ListenerRegistration? = null
    val db = Firebase.firestore
    private var messages = mutableListOf<D_Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)

        back = findViewById(R.id.back)
        fabSendMessage = findViewById(R.id.fabSendMessage)
        editMessage = findViewById(R.id.editMessage)
        rvChatList = findViewById(R.id.rvChatList)
        discussionImage = findViewById(R.id.imageDiscussion)
        discussionName = findViewById(R.id.nameFriend)

        discutionViewModel = ViewModelProvider(this).get(DisscussionViewModel::class.java)
        //Intialisation de  l'adapter
        discutionAdapter = DiscutionAdapter( Firebase.auth.currentUser!!.uid)
        //Configuration de l'adapter
       rvChatList.apply {
            layoutManager = LinearLayoutManager(this@Discussion)
            adapter = discutionAdapter
        }

        back.setOnClickListener {
            finish()
        }
        // Ajouter un Snapshot Listener pour écouter les nouvelles données
        listenerRegistration =
            db.collection("Disscussion").document("discussionId").collection("Messages")
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
                discutionViewModel.addMessage("id",message)
                editMessage.setText("")

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