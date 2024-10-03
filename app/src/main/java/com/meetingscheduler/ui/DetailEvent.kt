package com.meetingscheduler.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.Model.Event
import com.meetingscheduler.R
import com.meetingscheduler.ViewModels.FileViewModel
import com.meetingscheduler.adapters.FileAdapter

/*
  Activity  for displaying details of an event and its associated files.
   */
class DetailEvent : AppCompatActivity() {
    private lateinit var fileAdapter: FileAdapter
    private lateinit var fileViewModel: FileViewModel
    private lateinit var back: ImageView
    private lateinit var titreEvent: TextView
    private lateinit var descriptionEvent: TextView
    private lateinit var dateEvent: TextView
    private lateinit var listFiles: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)
        Log.d("DetailEvent", "onCreate called")

        //Intialize UI compontents
        initializeUI()

        // Fatech the event  passed  via the intent
        val event = intent.getParcelableExtra<Event>("event")

        // Display event details , set up RecyclerView and observer the changes
        event?.let {
            desplayedEventDetails(it)
            setupRecyclerVeiw()
            observeViewModel(it.id_event)
        } ?: run {
            // Handle missing event case
            Toast.makeText(this, "ID de l'événement manquant", Toast.LENGTH_SHORT).show()
            finish()
        }

        //Back button action
        back.setOnClickListener {
            finish()
        }
    }

    /**
     * Set up RecyclerView for displaying event files.
     */
    private fun setupRecyclerVeiw() {
        fileAdapter = FileAdapter(emptyList())
        listFiles.apply {
            layoutManager = GridLayoutManager(this@DetailEvent, 2)
            adapter = fileAdapter
        }
    }

    /**
     * Display event details in the UI.
     */
    private fun desplayedEventDetails(event: Event) {
        titreEvent.text = event.titre_Event
        descriptionEvent.text = event.description_Event
        dateEvent.text = "${event.jour_event} ${event.heure_event}"
    }

    /**
     * Initialize UI components.
     */
    private fun initializeUI() {
        back = findViewById(R.id.back)
        titreEvent = findViewById(R.id.titreEvent)
        descriptionEvent = findViewById(R.id.description)
        dateEvent = findViewById(R.id.date)
        listFiles = findViewById(R.id.fichier)

    }

    /**
     * Observe ViewModel data and update UI accordingly.
     */
    private fun observeViewModel(id: String?) {
        if (id.isNullOrEmpty()) {
            Toast.makeText(this, "ID de l'événement manquant", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        fileViewModel = ViewModelProvider(this).get(FileViewModel::class.java)
        // Fetch files for the event
        fileViewModel.getFiles(id)
        // Observe the LiveData and update the adapter when data changes
        fileViewModel.files.observe(this, Observer { files ->
            fileAdapter.updateFile(files.toMutableList())
        }
        )

    }
}