package com.meetingscheduler.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.Discussion
import com.meetingscheduler.app.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ListOfDiscussions(private val userViewModel: UserViewModel) : ViewModel() {
    private val _discussions = MutableLiveData<List<Discussion>>()
    val discussions: LiveData<List<Discussion>> = _discussions
    val db = Firebase.firestore

    suspend fun getDiscussions(id_user: String) = withContext(Dispatchers.IO) {
        try {

            val result =
                db.collection("ConcernedBYDiscussion").whereEqualTo("id_user", id_user).get()
                    .await()
            val listOfDiscussions = mutableListOf<Discussion>()

            if (result.isEmpty) {
                _discussions.postValue(listOfDiscussions)
                return@withContext
            }
            result.documents.forEach { doc ->
                val discussion = getDiscussion("dURoIFoM6yU9adwJRUYNfdWGB9B3")!!
                discussion.let {
                    listOfDiscussions.add(it)
                }
            }

            _discussions.postValue(listOfDiscussions)

        } catch (e: Exception) {

            Log.e("Firestore", "Erreur lors de la récupération des discussions", e)
        }
    }

    suspend fun getDiscussion(id_discussion: String): Discussion? {

        return try {
            val result = db.collection("Discussion").document(id_discussion).get().await()
            if (result.exists()) {
                Log.d("Firestore", "Discussion trouvée")
                val discussion = Discussion("", "", "", "", "")
                discussion.let {
                    discussion.id_disscussion = result.id
                    discussion.disHour = Utils.getHeur(result.getDate("date")!!)
                    discussion.type = result.getString("type") ?: ""
                    Log.d("Firestore", "Discussion trouvée 2")
                    if (discussion.type == "groupe") {
                        discussion.disName = result.getString("name").toString()
                        discussion.disImage = result.getString("photo").toString()
                        discussion.lastMessage = result.getString("lastMessage").toString()
                        Log.d("Firestore", "Discussion trouvée 3")
                    } else {
                        db.collection("Discussion").document(id_discussion).collection("members")
                            .get().addOnSuccessListener { members ->
                                val user2Doc =
                                    members.documents.firstOrNull { it.id != Firebase.auth.currentUser!!.uid }  // Filtrer user2
                                user2Doc?.let { member ->
                                    userViewModel.fetchUserAttributes(member.id)

                                    // Observer les attributs photo et name de l'utilisateur
                                    val photoObserver = Observer<String> { photo ->
                                        discussion.disImage = photo
                                        // Met à jour après avoir récupéré la photo

                                    }
                                    val nameObserver = Observer<String> { name ->
                                        discussion.disName = name
                                        // Met à jour après avoir récupéré le nom
                                    }
                                    userViewModel.userPhoto.observeForever { photo ->
                                        discussion.disImage = photo
                                    }
                                    userViewModel.userName.observeForever { name ->
                                        discussion.disName = name
                                    }
                                }
                            }
                    }
                    return discussion
                }

            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Erreur lors de la récupération de la discussion", e)
            null

        }
    }


}