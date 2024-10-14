package com.meetingscheduler.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.Discussion
import com.meetingscheduler.R
import com.meetingscheduler.ViewModels.ListOfDiscussions
import com.meetingscheduler.ViewModels.ListOfDiscussionsFactory
import com.meetingscheduler.ViewModels.UserViewModel
import com.meetingscheduler.adapters.ChatAdpater
import kotlinx.coroutines.launch

class Chat : Fragment() {
    //Firebase instances for firestore,Authentication
    val db = Firebase.firestore
    val auth = Firebase.auth
    val curentUser = auth.currentUser

    //Adapter ,ViewModel
    lateinit var chatAdapter: ChatAdpater
    private lateinit var listDiscussionViewModel: ListOfDiscussions
    private lateinit var userViewModel: UserViewModel

    //UI components
    private lateinit var rvDiscussion: RecyclerView
    private lateinit var btnAdd: ImageView
    private lateinit var txtEnseignants: TextView

    //MutableList to hold discussions
    val discussions = mutableListOf<Discussion>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_etudiant_chat, container, false)
        //Intailize ViewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        listDiscussionViewModel = ViewModelProvider(
            this,
            ListOfDiscussionsFactory(userViewModel)
        ).get(ListOfDiscussions::class.java)
        initComponents(view)
        //Intialize Adapter
        chatAdapter = ChatAdpater()
        //Set up recyclerView

        rvDiscussion.layoutManager = LinearLayoutManager(requireContext())
        rvDiscussion.adapter = chatAdapter
        chatAdapter.items = discussions
        //Load discussions for the current user
        curentUser.let {
            lifecycleScope.launch {
                listDiscussionViewModel.getDiscussions(it!!.uid)
                //Observe changes in discussions from viewModel
                listDiscussionViewModel.discussions.observe(
                    viewLifecycleOwner,
                    Observer { discussions ->
                        // Update the chat adapter with new discussions
                        Log.d("Chat", "New discussions: ${discussions.size}")
                        chatAdapter.updateDiscussions(discussions.toMutableList())
                    })
            }
        }
        // Set click listener for the add button
        btnAdd.setOnClickListener {

        }

        return view
    }

    private fun initComponents(view: View) {
        txtEnseignants = view.findViewById(R.id.txtEnseignants)
        rvDiscussion = view.findViewById(R.id.rvDiscussion)
        btnAdd = view.findViewById(R.id.btnAdd)
    }


}