package com.meetingscheduler.Model

data class User(
    var  id_user :String ,
    val nom :String,
    val prenom :String,
    val email:String,
    val role :String ,
   val type_user:String
)
