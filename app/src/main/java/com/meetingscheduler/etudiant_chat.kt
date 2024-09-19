package com.meetingscheduler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.adapters.DiscutionAdapter

class etudiant_chat : AppCompatActivity() {
    lateinit var list_chat : RecyclerView
    lateinit var chatAdapter: DiscutionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etudiant_chat)
    }
}