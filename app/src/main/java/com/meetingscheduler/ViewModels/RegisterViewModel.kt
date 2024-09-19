package com.meetingscheduler.ViewModels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Objects.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel:ViewModel() {
    private val auth = Firebase.auth
    private var _userExists = MutableLiveData<Boolean>()
    val userExists : LiveData<Boolean> = _userExists
    private var _emailVerified = MutableLiveData<Boolean>()
    val emailVerified: LiveData<Boolean> = _emailVerified
    private var _verificationTimeout = MutableLiveData<Boolean>()
    val verificationTimeout: LiveData<Boolean> = _verificationTimeout

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun checkIfUserExists(email: String) {
        _isLoading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getUtilisateurs() // Appel à l'API

                if (response.isSuccessful) {
                    val utilisateurs = response.body()
                    utilisateurs?.let {
                        // Vérifiez si l'utilisateur avec cet email existe
                        val userExists = utilisateurs.any { it.email == email }
                        withContext(Dispatchers.Main) {
                            _userExists.value = userExists
                            _isLoading.value = false
                        }
                    }
                } else {
                    _errorMessage.postValue("Erreur lors de la récupération des utilisateurs.")
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                _isLoading.postValue(false)
            }
        }
    }

    fun createUser(email: String, password: String) {
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendVerificationEmail()
                } else {
                    _errorMessage.value = "Erreur lors de la création du compte."
                    _isLoading.value = false
                }
            }
    }

    private fun sendVerificationEmail() {
        val user: FirebaseUser? = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkEmailVerificationWithTimeout()
            } else {
                _errorMessage.value = "Erreur lors de l'envoi de l'email de vérification."
                _isLoading.value = false
            }
        }
    }

    private fun checkEmailVerificationWithTimeout() {
        _isLoading.value = true
        val user: FirebaseUser? = auth.currentUser
        val maxTimeMillis = 300000L
        val checkInterval = 5000L

        val handler = Handler(Looper.getMainLooper())
        val startTime = System.currentTimeMillis()

        val runnable = object : Runnable {
            override fun run() {
                user?.reload()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (user.isEmailVerified) {
                            _emailVerified.value = true
                            _isLoading.value = false
                            handler.removeCallbacks(this)
                        } else {
                            val elapsedTime = System.currentTimeMillis() - startTime
                            if (elapsedTime >= maxTimeMillis) {
                                _verificationTimeout.value = true
                                _isLoading.value = false
                                handler.removeCallbacks(this)
                            } else {
                                handler.postDelayed(this, checkInterval)
                            }
                        }
                    } else {
                        _errorMessage.value = task.exception?.message
                        _isLoading.value = false
                        handler.removeCallbacks(this)
                    }
                }
            }
        }
        handler.post(runnable)
    }
}