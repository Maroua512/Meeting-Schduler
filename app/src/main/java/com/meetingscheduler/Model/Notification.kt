package com.meetingscheduler.Model

data class Notification(
    var id_notification : String = "",
    var titre_notification:String ="",
    var description_notification:String ="",
    var date_notification :String ="",
    var image_notification:String ="",
    var type_notification: String = "",
    var sender_id: String = "",

)
