package com.meetingscheduler.Model

data class Day(
    val dayNumber: Int,  // Numéro du jour
    val isToday: Boolean = false,  // Est-ce le jour actuel ?
    val hasEvent: Boolean = false,  // Le jour a-t-il des événements ?
    val hasMeeting: Boolean = false,  // Réunion
    val hasAssignment: Boolean = false,  // Devoir
    val hasInfo: Boolean = false  // Information
)
