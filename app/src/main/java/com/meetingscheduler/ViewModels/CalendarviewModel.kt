package com.meetingscheduler.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.meetingscheduler.Model.Day
import com.meetingscheduler.Model.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarviewModel : ViewModel() {
    // Intailisation de Firebase ,Firestore et Auth
    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    // LiveData qui contient la liste des jours
    private val _days = MutableLiveData<List<Day>>()
    val days: LiveData<List<Day>> get() = _days

    //LiveData  qui contient la  list des evenement
    private val _toDayEvent = MutableLiveData<List<Event>>()
    val toDayEvent: LiveData<List<Event>> get() = _toDayEvent

    //LiveData  qui contient la  list des evenement
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events


    fun genererDaysForMonth() {
        val days = mutableListOf<Day>()
        var isToday: Boolean
        var eventsForDay: MutableList<Event>
        var hasMeeting: Boolean
        var hasAssignment: Boolean
        var hasInfo: Boolean

        // Récupérer la date actuelle
        val calendar = Calendar.getInstance()
        // Récupérer le nombre de jours dans le mois
        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Récupérer les événements

        for (i in 1..maxDays) {
            // Mettre à jour le jour du mois pour chaque itération
            calendar.set(Calendar.DAY_OF_MONTH, i)

            // Convertir la date actuelle dans la boucle en chaîne
            val currentDateString = getJour(calendar.time)

            // Vérifier si c'est aujourd'hui
            isToday = (i == Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

            // Ajouter le jour à la liste
            days.add(
                Day(
                    dayNumber = i,
                    isToday = isToday,
                    hasEvent = false,
                    hasMeeting = false,
                    hasAssignment = false,
                    hasInfo = false
                )
            )
        }

        // Mettre à jour la liste des jours après avoir traité tous les événements
        _days.value = days

        // Récupérer les événements
        getEvents { events ->
            val updateDays = days.map { day ->
                //Convertir le jour en chaine de carctere  pour la comparaison avec la date des evenements
                calendar.set(Calendar.DAY_OF_MONTH, day.dayNumber)
                val currentDay = getJour(calendar.time)
                //Filtrer  les  jours  pour le jour courant
                eventsForDay = events.filter { event ->
                    event.jour_event == currentDay
                }.toMutableList()
                //Vérifier si il y a des événements pour ce jour
                hasMeeting = eventsForDay.any { event ->
                    event.type_Event == "meeting"
                }
                hasInfo = eventsForDay.any { event ->
                    event.type_Event == "info"
                }
                hasAssignment = eventsForDay.any { event ->
                    event.type_Event == "assignement"
                }
                // Mettre à jour les informations du jour avec les événements
                day.copy(
                    hasEvent = hasMeeting || hasAssignment || hasInfo,
                    hasMeeting = hasMeeting,
                    hasAssignment = hasAssignment,
                    hasInfo = hasInfo
                )
            }
            // Mettre à jour la liste des jours avec les événements
            _days.value = updateDays
            // Si c'est aujourd'hui, mettre à jour la liste des événements du jour
            val toDay = events.filter { event ->
                event.jour_event == getJour(Calendar.getInstance().time)
            }
            _toDayEvent.value = toDay
            _events.value = events
        }

    }


    fun getEvents(callback: (MutableList<Event>) -> Unit) {
        val events = mutableListOf<Event>()
        db.collection("Event")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    db.collection("Event").document(doc.id).collection("Participants")
                        .document(currentUser!!.uid).get().addOnSuccessListener {
                            val event = Event("", "", "", "", "", "")

                            // Récupère les valeurs des champs
                            event.id_event = doc.id
                            event.titre_Event =
                                doc.getString("titre") ?: "" // Vérifie si le champ "titre" existe
                            event.type_Event = doc.getString("type") ?: ""
                            event.description_Event = doc.getString("description") ?: ""
                            val date = doc.getDate("date")
                            // Vérifier si le timestamp n'est pas null
                            if (date != null) {
                                // Récupère une date si nécessaire
                                event.jour_event = getJour(date)
                                event.heure_event = getHeur(date)
                                // Tu peux ensuite utiliser cet objet `event`
                            } else {
                                Log.d("getEvents", "No such document")
                            }
                            events.add(event)
                            callback(events)
                        }.addOnFailureListener {
                            Log.e("Event", "No Event  , Erruer")
                        }
                }
                // Appeler le callback avec la liste des événements

                //callback(events)
            }.addOnFailureListener {
                // Appeler le callback avec la liste des événements
                //callback(events)
                Log.d("getEvents", "Error getting documents: ", it)
            }
    }

    // Fonction pour convertir la date en chaîne de caractères
    fun getJour(date: Date): String {
        return SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
    }

    fun getHeur(date: Date): String {
        return SimpleDateFormat("HH:MM", Locale.getDefault()).format(date)

    }

}