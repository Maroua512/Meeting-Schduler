package com.meetingscheduler.ViewModels
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.User
import com.meetingscheduler.Objects.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// ViewModel pour la gestion de l'enregistrement des utilisateurs
class RegisterViewModel : ViewModel() {

    // Initialisation de FirebaseAuth pour gérer les utilisateurs
    private val auth = Firebase.auth

    // LiveData pour indiquer si l'utilisateur existe déjà
    private var _userExists = MutableLiveData<Boolean>()
    val userExists: LiveData<Boolean> = _userExists // Version publique en lecture seule

    // LiveData pour vérifier si l'email de l'utilisateur a été validé
    private var _emailVerified = MutableLiveData<Boolean>()
    val emailVerified: LiveData<Boolean> = _emailVerified

    // LiveData pour gérer le délai d'expiration de la vérification de l'email
    private var _verificationTimeout = MutableLiveData<Boolean>()
    val verificationTimeout: LiveData<Boolean> = _verificationTimeout

    // LiveData pour stocker un message d'erreur en cas d'échec de l'opération
    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // LiveData pour stocker un message d'erreur en cas d'échec de l'opération
    private var _founderUser = MutableLiveData<User?>()
    val founderUser: LiveData<User?> = _founderUser


    // LiveData pour indiquer si une opération est en cours (chargement)
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Rôle : cette fonction effectue une requête à l'API pour voir si un utilisateur avec cet email existe déjà dans la base de donnees Scolarite.
    // Si l'utilisateur est trouvé, la variable `userExists` est mise à jour avec `true`. Sinon, elle est mise à jour avec `false`.
    @OptIn(DelicateCoroutinesApi::class)
    fun checkIfUserExists(email: String) {
        _isLoading.value = true // Indiquer que l'opération commence
        GlobalScope.launch(Dispatchers.IO) { // Exécuter la tâche en arrière-plan
            try {
                // Appel à l'API pour récupérer la liste des utilisateurs
                val response = RetrofitInstance.api.getUtilisateurs()

                if (response.isSuccessful) {
                    // Si la réponse est un succès, obtenir les utilisateurs
                    val utilisateurs = response.body()
                    utilisateurs?.let {
                        // Vérifier si l'email existe dans la liste des utilisateurs
                        val user = utilisateurs.find { it.email == email }
                        _userExists.postValue(user != null)
                        _founderUser.postValue(user)
                        _isLoading.postValue(false)
                        // Mise à jour des variables sur le thread principal

                    }
                } else {
                    // Si la réponse échoue, mettre un message d'erreur
                    _errorMessage.postValue("Erreur lors de la récupération des utilisateurs.")
                    _isLoading.postValue(false) // Arrêter le chargement
                }
            } catch (e: Exception) {
                // Gestion des exceptions et affichage du message d'erreur
                _errorMessage.postValue(e.message)
                _isLoading.postValue(false) // Arrêter le chargement
            }
        }
    }

    // Rôle : cette fonction utilise Firebase pour créer un compte avec les informations fournies (email et mot de passe).
    // Si la création réussit, elle envoie un email de vérification.
    fun createUser(email: String, password: String) {
        _isLoading.value = true // Démarrer l'opération de chargement
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Si la création de l'utilisateur réussit, envoyer un email de vérification
                    sendVerificationEmail()
                } else {
                    // En cas d'échec, afficher un message d'erreur
                    _errorMessage.value = "Erreur lors de la création du compte."
                    _isLoading.value = false // Arrêter le chargement
                }
            }
    }

    // Rôle : cette fonction envoie un email de vérification à l'utilisateur après la création de son compte.
    // Elle déclenche également le processus pour vérifier si l'email a été validé.
    private fun sendVerificationEmail() {
        val user: FirebaseUser? = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Si l'envoi de l'email est un succès, commencer la vérification avec délai d'attente
                checkEmailVerificationWithTimeout()
            } else {
                // En cas d'échec de l'envoi, afficher un message d'erreur
                _errorMessage.value = "Erreur lors de l'envoi de l'email de vérification."
                _isLoading.value = false // Arrêter le chargement
            }
        }
    }

    // Rôle : cette fonction vérifie régulièrement si l'utilisateur a validé son email.
    // Elle définit un délai d'attente (5 minutes) et vérifie périodiquement si l'email a été validé, en utilisant un Handler.
    private fun checkEmailVerificationWithTimeout() {
        _isLoading.value = true // Démarrer l'opération de chargement
        val user: FirebaseUser? = auth.currentUser
        val maxTimeMillis = 300000L // Délai d'attente maximum de 5 minutes (300 000 ms)
        val checkInterval = 5000L // Vérifier toutes les 5 secondes (5000 ms)

        // Utilisation d'un Handler pour exécuter des tâches périodiques
        val handler = Handler(Looper.getMainLooper())
        val startTime = System.currentTimeMillis() // Enregistrer le temps de début

        // Définition d'un Runnable pour exécuter la vérification périodique
        val runnable = object : Runnable {
            override fun run() {
                // Recharger les données de l'utilisateur pour vérifier la validation de l'email
                user?.reload()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Si l'email est vérifié, arrêter l'opération
                        if (user.isEmailVerified) {
                            _emailVerified.value = true // Email vérifié
                            _isLoading.value = false // Arrêter le chargement
                            handler.removeCallbacks(this) // Arrêter le Runnable
                        } else {
                            // Vérifier si le délai d'attente est dépassé
                            val elapsedTime = System.currentTimeMillis() - startTime
                            if (elapsedTime >= maxTimeMillis) {
                                _verificationTimeout.value = true // Délai d'attente dépassé
                                _isLoading.value = false // Arrêter le chargement
                                handler.removeCallbacks(this) // Arrêter le Runnable
                            } else {
                                // Replanifier la vérification après l'intervalle défini
                                handler.postDelayed(this, checkInterval)
                            }
                        }
                    } else {
                        // En cas d'erreur, afficher le message d'erreur
                        _errorMessage.value = task.exception?.message
                        _isLoading.value = false // Arrêter le chargement
                        handler.removeCallbacks(this) // Arrêter le Runnable
                    }
                }
            }
        }
        // Démarrer le Runnable
        handler.post(runnable)
    }
}
