package com.meetingscheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.meetingscheduler.Model.D_Friend
import com.meetingscheduler.Model.Discussion
import com.meetingscheduler.Model.Disscusion_user
import com.meetingscheduler.adapters.ChatAdpater
import com.meetingscheduler.ui.Chat
import com.meetingscheduler.ui.MainActivity
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Chat_teatcher : Fragment() {
    lateinit var btnAddGroupe:ImageView
    lateinit var rvEnsiegnant: RecyclerView
    lateinit var textEtudiant:TextView
    lateinit var chatAdapter: ChatAdpater
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var discussionGroupe: LinearLayout
    private var snapshotListeners: MutableList<ListenerRegistration> = mutableListOf()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val   view = inflater.inflate(R.layout.activity_chat_enseignant, container, false)
        textEtudiant = view.findViewById(R.id.txtClients)
        btnAddGroupe = view.findViewById(R.id.btnAddGroupe)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        currentUser = auth.currentUser
        rvEnsiegnant = view.findViewById(R.id.rvEnsiegant)
        textEtudiant.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(Chat())
        }
        btnAddGroupe.setOnClickListener {

        }
        chatAdapter = ChatAdpater()
        rvEnsiegnant.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        synchronizeDiscussions()
       return view
    }
    suspend fun getDiscussionsForCurrentUser(): MutableList<Discussion> {
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

        val discussions = mutableListOf<Discussion>()
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

                val discussion = Discussion(
                     discussionObj.id,
                   lastMessageText,
                    getNames(discussionUser.id_user),
                    if (lastMessageTimestamp > 0) {
                        SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(lastMessageTimestamp))
                    } else "",
                 " getImage(discussionUser.id_user)"
                )
                discussions.add(discussion)

            } catch (e: Exception) {
                println("Erreur lors de la récupération des discussions: $e")
            }
        }
        return discussions
    }
    private fun synchronizeDiscussions() {
        runBlocking {
            val friends = getDiscussionsForCurrentUser()
            chatAdapter.items = friends
            chatAdapter.notifyDataSetChanged()

            // Setup listeners for real-time updates
            friends.forEach { discussion ->
                val listenerRegistration = db.collection("Disscussion")
                    .document(discussion.id_disscussion)
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
                            discussion.lastMessage = lastMessage
                            discussion.disHour = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                                .format(lastMessageDoc.getDate("timestamp") ?: Date())

                            chatAdapter.notifyDataSetChanged()
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
    }


}
