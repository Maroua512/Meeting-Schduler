package com.meetingscheduler.Model

data class Event(
    var id_event : String = "",
    var jour_event   : String,
    var heure_event : String,
    var titre_Event : String,
    var description_Event : String,
    var type_Event: String
)
