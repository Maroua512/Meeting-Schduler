package com.marouasapplication.app.modules.discution.ui

class DiscutionActivity  {
 /* private lateinit var auth: FirebaseAuth
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

  @SuppressLint("SetTextI18n")
  override fun onInitialized() {
    auth = FirebaseAuth.getInstance()
    db = FirebaseFirestore.getInstance()
    currentUser = auth.currentUser

    back = findViewById(R.id.back)
    fabSendMessage = findViewById(R.id.fabSendMessage)
    editMessage = findViewById(R.id.editMessage)
    rvChatList = findViewById(R.id.rvChatList)
    discussionImage = findViewById(R.id.imageDiscussion)
    discussionName = findViewById(R.id.nameFriend)

    val typeDiscussion = intent.getStringExtra("typeDisscussion").toString()
    val discussionId = if (typeDiscussion == "groupe") "dg" else intent.getStringExtra("id_discussion").toString()
    discussionName.text = if (typeDiscussion == "groupe") {
      "Groupe Prestataires"
    } else {
      intent.getStringExtra("friendName").toString()
    }
    if (typeDiscussion == "groupe") {
      discussionImage.setImageResource(R.drawable.img_people)
    } else {
      val friendImage = intent.getStringExtra("friendImage").toString()
      Glide.with(this).load(friendImage).placeholder(R.drawable.img_lock).into(discussionImage)
    }

    back.setOnClickListener {
      finish()
    }

    discutionAdapter = DiscutionAdapter(currentUser!!.uid)
    rvChatList.layoutManager = LinearLayoutManager(this)
    rvChatList.adapter = discutionAdapter

    // Ajouter un Snapshot Listener pour écouter les nouvelles données
    listenerRegistration = db.collection("Disscussion").document(discussionId).collection("Messages")
      .orderBy("timestamp")
      .addSnapshotListener { snapshot, e ->
        if (e != null) {
          Log.e(TAG, "Erreur de chargement des messages", e)
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
        db.collection("Disscussion").document(discussionId).collection("Messages").add(data)
          .addOnSuccessListener {
            editMessage.setText("")
          }.addOnFailureListener {
            Log.e(TAG, "Erreur lors de l'envoi du message", it)
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

  override fun setUpClicks() {}

  companion object {
    const val TAG: String = "DISCUTION_ACTIVITY"
  }*/
}
