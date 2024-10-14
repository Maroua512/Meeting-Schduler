package com.meetingscheduler.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.Discussion
import com.meetingscheduler.Model.Message
import com.meetingscheduler.R
import com.meetingscheduler.ViewModels.DisscussionViewModel
import com.meetingscheduler.adapters.DiscutionAdapter

class Discussion : AppCompatActivity() {
    //Firebase Firestore and Authentification instances
    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    //UI components
    private lateinit var discussionViewModel: DisscussionViewModel
    private lateinit var discussionAdapter: DiscutionAdapter
    private lateinit var back: ImageView
    private lateinit var fabSendMessage: FloatingActionButton
    private lateinit var editMessage: EditText
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var discussionName: TextView
    private lateinit var discussionImage: ImageView
    private lateinit var discussion: Discussion

    //List to hold chat messages
    private var messages = mutableListOf<Message>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)
        //Intialize UI components
        initUIComponents()

        //Set up the viewModel  for handling messages
        setupViewModel()

        //Set up RecylerView and its adapter for displaying messages
        setupRecyclerView()

        //Handle back button action
        handleBackButton()

        // Handle send message action
        handleSendMessageAction(discussion.id_disscussion)

    }

    private fun initUIComponents() {
        back = findViewById(R.id.back)
        fabSendMessage = findViewById(R.id.fabSendMessage)
        editMessage = findViewById(R.id.editMessage)
        chatRecyclerView = findViewById(R.id.rvChatList)
        discussionImage = findViewById(R.id.imageDiscussion)
        discussionName = findViewById(R.id.nameFriend)
    }

    /**
     * Set up the ViewModel for managing message data.
     */
    private fun setupViewModel() {
        discussionViewModel = ViewModelProvider(this).get(DisscussionViewModel::class.java)

        // Get discussion ID from intent and load messages
        discussion = intent.getParcelableExtra<Discussion>("discussion") as Discussion
        discussionViewModel.getMessages(discussion.id_disscussion)
        discussionName.text = discussion.disName
        Glide.with(this).load(discussion.disImage).placeholder(R.drawable.profil_vide)
            .into(discussionImage)

        // Observe message list updates
        discussionViewModel.messages.observe(this) { messages ->
            discussionAdapter.updateMessages(messages.toMutableList())
        }
    }

    /**
     * Set up the RecyclerView and adapter for displaying chat messages.
     */
    private fun setupRecyclerView() {
        discussionAdapter = DiscutionAdapter(currentUser!!.uid)
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Discussion)
            adapter = discussionAdapter
        }

        // Set the initial empty message list
        discussionAdapter.items = messages
    }

    /**
     * Handle the back button action to close the activity.
     */
    private fun handleBackButton() {
        back.setOnClickListener {
            finish()
        }
    }

    /**
     * Handle the send message action when the floating button is clicked.
     */
    private fun handleSendMessageAction(id_disscussion: String) {
        fabSendMessage.setOnClickListener {
            val messageText = editMessage.text.toString()

            if (messageText.isNotEmpty()) {
                // Send message via ViewModel
                discussionViewModel.addMessage(id_disscussion, messageText)
                // Scroll to the last message
                discussionViewModel.messages.observe(this, Observer {
                    discussionAdapter.updateMessages(it.toMutableList())
                })
                chatRecyclerView.smoothScrollToPosition(discussionAdapter.itemCount - 1)

                // Clear input field after sending
                editMessage.setText("")
            }
        }
    }
}
