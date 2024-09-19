package com.meetingscheduler

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.meetingscheduler.adapters.ChatAdpater


class chat_enseignant : AppCompatActivity() {
    lateinit var rvEnsiegnant: RecyclerView
    lateinit var chatAdapter: ChatAdpater
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var discussionGroupe: LinearLayout
    private var snapshotListeners: MutableList<ListenerRegistration> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_enseignant)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = auth.currentUser
       // rvEnsiegnant = findViewById()

        }
    }
