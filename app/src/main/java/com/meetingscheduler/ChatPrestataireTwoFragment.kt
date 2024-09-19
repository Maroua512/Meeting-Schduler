package com.marouasapplication.app.modules.chatprestatairetwo.ui

import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

//import com.meetingscheduler.ChatPrestataireTwoAdapter
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatPrestataireTwoFragment  {
   /* lateinit var rvFriend: RecyclerView
    lateinit var chatClientAdapter: ChatClientAdapter
    lateinit var txtClients: TextView
    lateinit var btnJoin: Button
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var discussionGroupe: LinearLayout
    private var snapshotListeners: MutableList<ListenerRegistration> = mutableListOf()
    private val VISIBLE_THRESHOLD = 5
    private var isLoading = false
    private var lastVisibleMessageTimestamp: Long? = null


    override fun onInitialized() {
        rvFriend = binding.root.findViewById(R.id.rvFriend)
        txtClients = binding.root.findViewById(R.id.txtClients)

        discussionGroupe = binding.root.findViewById(R.id.discussionGroupe)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = auth.currentUser

        txtClients.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(ChatPrestataireOneFragment())
        }

        discussionGroupe.setOnClickListener {
            Intent(requireContext(), DiscutionActivity::class.java).also {
                it.putExtra("typeDisscussion", "groupe")
                startActivity(it)
            }
        }

        chatClientAdapter = ChatPrestataireTwoAdapter()

        rvFriend.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatClientAdapter
        }

        synchronizeDiscussions()

    }

    suspend fun getDiscussionsForCurrentUser(): MutableList<D_Friend> {
        val membreDiscussions = db.collection("Concerner_Discussion_Prestataire")
            .whereEqualTo("id_user1", currentUser!!.uid)
            .whereEqualTo("type_discussion","prestataire")
            .get().await()
            .documents
            .union(
                db.collection("Concerner_Discussion_Prestataire")
                    .whereEqualTo("id_user2", currentUser!!.uid)
                    .whereEqualTo("type_discussion","prestataire")
                    .get().await()
                    .documents
            )

        val discussionUsers = mutableListOf<Disscusion_user>()

        membreDiscussions.forEach { document ->
            val user1Id = document.getString("id_user1")
            val user2Id = document.getString("id_user2")

            val otherUserId = if (user1Id == currentUser!!.uid) user2Id else user1Id
            if (otherUserId != null) {
                val discussionUser = Disscusion_user(
                    id_user = otherUserId,
                    discussionId = document.id
                )
                discussionUsers.add(discussionUser)
            }
        }

        val discussions = mutableListOf<D_Friend>()
        for (discussionUser in discussionUsers) {
            try {
                val discussionObj = db.collection("Disscussion")
                    .document(discussionUser.discussionId)
                    .get().await()

                // Récupérer le dernier message de la discussion
                val messagesRef = db.collection("Disscussion")
                    .document(discussionUser.discussionId)
                    .collection("Messages")
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(1) // Limiter à 1 pour obtenir le dernier message
                    .get().await()

                val lastMessage = messagesRef.documents.firstOrNull()
                val lastMessageText = lastMessage?.getString("texte") ?: ""
                val lastMessageTimestamp = lastMessage?.getDate("timestamp")?.time ?: 0L

                val discussion = D_Friend(
                    id_discussion = discussionObj.id,
                    lastMsg = lastMessageText,
                    name = getNames(discussionUser.id_user),
                    timestamp = if (lastMessageTimestamp > 0) {
                        SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(lastMessageTimestamp))
                    } else "",
                    image = getImage(discussionUser.id_user)
                )
                discussions.add(discussion)

            } catch (e: Exception) {
                println("Erreur lors de la récupération des discussions: $e")
            }
        }
        return discussions
    }*/
    /*private fun synchronizeDiscussions() {
        runBlocking {
            val friends = getDiscussionsForCurrentUser()
            chatClientAdapter.items = friends
            chatClientAdapter.notifyDataSetChanged()

            // Setup listeners for real-time updates
            friends.forEach { discussion ->
                val listenerRegistration = db.collection("Disscussion")
                    .document(discussion.id_discussion!!)
                    .collection("Messages")
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            println("Listen failed: $e")
                            return@addSnapshotListener
                        }

                        if (snapshot != null && !snapshot.isEmpty) {
                            val lastMessageDoc = snapshot.documents[0]
                            val lastMessage = lastMessageDoc.getString("texte") ?: ""
                            discussion.lastMsg = lastMessage
                            discussion.timestamp = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                                .format(lastMessageDoc.getDate("timestamp") ?: Date())

                            chatClientAdapter.notifyDataSetChanged()
                        }
                    }
                snapshotListeners.add(listenerRegistration)
            }
        }
    }


    private suspend fun getImage(idUser: String): String? {
        return try {
            val document = db.collection("Utilisateur").document(idUser).get().await()
            if (document.exists()) {
                document.getString("photo_profile").toString()
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    private suspend fun getNames(idUser: String): String {
        return try {
            val document = db.collection("Utilisateur").document(idUser).get().await()
            if (document.exists()) {
                document.getString("nom") ?: ""
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }*/
  /*  private fun loadMoreMessages(id: String) {
        isLoading = true

        // Récupérer les messages après le dernier message visible
        val query = db.collection("Disscussion")
            .document(id) // Remplacez avec l'ID de la discussion actuelle
            .collection("Messages")
            .apply {
                lastVisibleMessageTimestamp?.let {
                    whereGreaterThan("timestamp", it)
                }
            }
            .orderBy("timestamp")
            .limit(1) // Limiter le nombre de messages chargés à la fois

        query.get().addOnSuccessListener { result ->
            val newMessages = result.map { document ->
                document.toObject(D_Message::class.java)
            }
            if (newMessages.isNotEmpty()) {
                lastVisibleMessageTimestamp = newMessages.last().timestamp
                chatClientAdapter.items.addAll(newMessages)
                chatClientAdapter.notifyDataSetChanged()
            }
            isLoading = false
        }.addOnFailureListener { e ->
            Log.e(TAG, "Erreur de chargement des messages", e)
            isLoading = false
        }
    }*/

   /* override fun onDestroy() {
        super.onDestroy()
        // Remove all snapshot listeners to avoid memory leaks
        snapshotListeners.forEach { it.remove() }
    }*/

   /* override fun setUpClicks() {
    }*/

    companion object {
        const val TAG: String = "CHAT_PRESTATAIRETWO_ACTIVITY"
    }
}
