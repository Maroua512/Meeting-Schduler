package com.meetingscheduler.Interfaces
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MailgunService {
    @FormUrlEncoded
    @POST("sandboxd63e20d5a39945fcbe6eac3b6b2a9f94.mailgun.org/messages")
    fun sendEmail(
        @Field("from") from: String,
        @Field("to") to: String,
        @Field("subject") subject: String,
        @Field("text") text: String
    ): Call<Void>
}
