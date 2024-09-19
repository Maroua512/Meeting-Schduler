package com.meetingscheduler

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.D_Friend
import com.meetingscheduler.adapters.ChatAdpater


class Chat : Fragment() {
    lateinit var list_chat : RecyclerView
    lateinit var chatAdapter: ChatAdpater
    lateinit var btnAdd:ImageView
    val db = Firebase.firestore
    val auth = Firebase.auth
    val curentUser= auth.currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.activity_etudiant_chat, container, false)
        list_chat = view.findViewById(R.id.rvFriend)
        btnAdd =view.findViewById(R.id.btnAdd)
        val discussions  = mutableListOf<D_Friend>()
        chatAdapter = ChatAdpater()
        if(curentUser!=null){
            db.collection("User_Disscussion").whereEqualTo("id_user",curentUser.uid).get().addOnSuccessListener { result->
               for (doc in result){
                   db.collection("Disscussion").document(doc.id).get().addOnSuccessListener {
                       val discussion = result.toObjects(D_Friend::class.java)
                       if(discussion!=null){
                           
                           //discussions.add(discussion)
                       }

                   }.addOnFailureListener {

                   }
               }

            }.addOnFailureListener {

            }
        }


        list_chat.layoutManager = LinearLayoutManager(requireContext())
        list_chat.adapter = chatAdapter
        chatAdapter.items =discussions
        val message = hashMapOf<String,Any>(

        )
        btnAdd.setOnClickListener {
            db.collection("Disscussion").document().collection("Messages").add(message).addOnSuccessListener {
                Toast.makeText(requireContext(),"votre groupe a ete bien cree",Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Erreur lors de creation d'un nouveau groupe",Toast.LENGTH_LONG).show()
            }
        }






        return view
    }


}