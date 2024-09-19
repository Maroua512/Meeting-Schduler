package com.meetingscheduler

import com.meetingscheduler.Model.D_Message

data class Disscussion(
    var  id_disscussion :String ,
    val disImage:String,
    val disName:String,
    val list: MutableList<D_Message>
)
