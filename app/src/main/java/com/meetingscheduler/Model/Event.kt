package com.meetingscheduler.Model

import android.os.Parcel
import android.os.Parcelable

data class Event(
    var id_event: String = "",
    var jour_event: String,
    var heure_event: String,
    var titre_Event: String,
    var description_Event: String,
    var type_Event: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id_event)
        dest.writeString(jour_event)
        dest.writeString(heure_event)
        dest.writeString(titre_Event)
        dest.writeString(description_Event)
        dest.writeString(type_Event)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(source: Parcel?): Event {
            return Event(source!!)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }

}
