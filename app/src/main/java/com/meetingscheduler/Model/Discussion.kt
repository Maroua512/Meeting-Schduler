package com.meetingscheduler.Model

import android.os.Parcel
import android.os.Parcelable

data class Discussion(
    var id_disscussion: String = "",
    var disImage: String = "",
    var disName: String = "",
    var type: String = "",
    var disHour: String = "",
    var lastMessage: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id_disscussion)
        dest.writeString(disImage)
        dest.writeString(disName)
        dest.writeString(type)
        dest.writeString(disHour)
        dest.writeString(lastMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Discussion> {
        override fun createFromParcel(source: Parcel?): Discussion {
            return Discussion(source!!)
        }

        override fun newArray(size: Int): Array<Discussion?> {
            return arrayOfNulls(size)
        }
    }
}
