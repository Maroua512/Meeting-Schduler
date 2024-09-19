package com.meetingscheduler.Model

data class D_Message(
    var sender: String,
    var texte: String,
    var timestamp: Long
){
    constructor():this("","",0)
}
