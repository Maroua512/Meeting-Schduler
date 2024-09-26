package com.meetingscheduler.ui


import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.R
import com.meetingscheduler.ViewModels.FileViewModel
import com.meetingscheduler.adapters.FileAdapter


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

        //intailisation des variables
        back = findViewById(R.id.back)
        titreEvent = findViewById(R.id.titreEvent)
        descriptionEvent = findViewById(R.id.description)
        dateEvent = findViewById(R.id.date)
        listFiles = findViewById(R.id.fichier)

        // Récupération de l'id de l'event
           val id_event = intent.getStringExtra("id_event")

        // Vérification si l'id_event est null
        if (id_event.isNullOrEmpty()) {
               Toast.makeText(this, "ID de l'événement manquant", Toast.LENGTH_SHORT).show()
               finish()
               return
           }

        //Intialisation de viewModel
         fileViewModel = ViewModelProvider(this).get(FileViewModel::class.java)

        //Intialisation de adapter
          fileAdapter = FileAdapter(emptyList())

        // Intailisation de RecyclerView
         listFiles.apply {
              layoutManager = LinearLayoutManager(this@DetailEvent)
              adapter = fileAdapter

          }

        //Action sur le button back
         back.setOnClickListener {
             finish()
         }

        // Mise à jour du LiveData (files) dans fileViewModel avec l'id_event
          fileViewModel.getFiles(id_event)

        //Observer  les changement de la liste des fichiers
          fileViewModel.files.observe(this, Observer { files ->
              // Mettre à jour l'adaptateur avec les nouvelles données
              Toast.makeText(this, "${files.size}", Toast.LENGTH_SHORT).show()
              fileAdapter.updateFile(files)
          })
    }
}