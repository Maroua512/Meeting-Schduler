package com.meetingscheduler.Model

data class Message(
    var id: String,
    var sender: String,
    var texte: String,
    var timestamp: String
){
    constructor():this("","","","")
}
