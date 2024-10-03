package com.meetingscheduler.Model

import android.os.Parcelable
import android.os.Parcel

data class User(
    var id_user: String,
    var nom: String,
    var prenom: String,
    var email: String,
    var role: String,
    var type_user: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_user)
        parcel.writeString(nom)
        parcel.writeString(prenom)
        parcel.writeString(email)
        parcel.writeString(role)
        parcel.writeString(type_user)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(source: Parcel): User {
            return User(source)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }

    }
}