package com.meetingscheduler.adapters
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.Model.Event
import com.meetingscheduler.R
import com.meetingscheduler.ui.DetailEvent

class EventAdapter(private var events:List<Event>):RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    inner class EventViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val  heureEvent : TextView =itemView.findViewById(R.id.heureEvent)
        val  titreEvent:TextView = itemView.findViewById(R.id.titreEvent)
        val descriptionEvent : TextView = itemView.findViewById(R.id.descriptionEvent)
        val  cardEvent : CardView = itemView.findViewById(R.id.cardEvent)
        val  notification : ImageView = itemView.findViewById(R.id.ic_notification)

        fun bind(event: Event){
            heureEvent.text = event.heure_event
            titreEvent.text = event.titre_Event
            descriptionEvent.text = event.description_Event
            if(event.type_Event == "meeting"){

                heureEvent.setTextColor(itemView.context.getColor(R.color.red_600))
                cardEvent.setCardBackgroundColor(itemView.context.getColor(R.color.red_600))
            }
            else if(event.type_Event == "info"){
                heureEvent.setTextColor(itemView.context.getColor(R.color.blue_200))
                cardEvent.setCardBackgroundColor(itemView.context.getColor(R.color.blue_200))
            }
            else{
                heureEvent.setTextColor(itemView.context.getColor(R.color.teal_200))
                cardEvent.setCardBackgroundColor(itemView.context.getColor(R.color.teal_200))
            }
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailEvent::class.java)
                intent.putExtra("event", event)
                it.context.startActivity(intent)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_card,parent,false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount()= events.size

    fun updateEvents(newEvent : List<Event>){
        events = newEvent
        notifyDataSetChanged()
    }
}
