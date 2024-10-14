package com.meetingscheduler.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.Model.Message
import com.meetingscheduler.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adapter class for displaying discussion messages in a recylerview
 */
class DiscutionAdapter(
    private val currentUserId: String  // ID of the current user  to defferentiate between the sender and the reciever of the message
) : RecyclerView.Adapter<DiscutionAdapter.MessageViewHolder>() {

    //List of messages to be displayed in the recylcerView
    var items: MutableList<Message> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged() //Notify the adapter that  the data has changed and the UI needs to be updated
        }

    /**
     * ViewHolder class to represent each chat message item in recyclerView
     */
    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //TextView  for displaying the message texte
        private val textViewMessage: TextView = view.findViewById(R.id.tvMsg)

        //TextView for displaying the time the message was sent
        private val tvHour: TextView = view.findViewById(R.id.tvHour)

        //Bind the data (Message object ) to the view (texteViews)
        fun bind(message: Message) {
            // Set the message text in the TextView
            textViewMessage.text = message.texte
            // Format the timestamp to a readable time (HH:mm format) and display it
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            tvHour.text = message.timestamp
        }
    }

    /**
     * Determine the view type  for each message(sent or received)
     */
    override fun getItemViewType(position: Int): Int {
        val message = items[position]
        return if (message.sender == currentUserId) {
            VIEW_TYPE_SENT  // If the sender message is the currentUser ,It's a sent message
        } else {
            VIEW_TYPE_RECEIVED  // Otherwise ,It's received message
        }
    }

    /**
     * Inflate the appropriate layout for the message based on the view type(sent or received)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        //Choose the layout on based whether the message is sent or received
        val layoutId = if (viewType == VIEW_TYPE_SENT)
            R.layout.item_chat_right  // Right-aligned layout for sent Messages
        else R.layout.item_chat_left  // Left-alignes layout for received Messages
        //Inflate the selected layout and create the viewHolder
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view)
    }

    /**
     * Return the number of the messages in the list
     */
    override fun getItemCount() = items.size

    /**
     * Bind the data  (Message object ) to the viewHolder for display
     */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        return holder.bind(items[position])
    }

    /**
     * update the  messages in the adapter and  refresh the UI
     */
    fun updateMessages(newMessages: MutableList<Message>) {
        items = newMessages  //Replace the old messages with the new ones
        notifyDataSetChanged()  // Notify the adapter to update the recyclerview
    }

    /**
     * Companion object to defines cosntants for view types (sent and received)
     */
    companion object {
        private const val VIEW_TYPE_SENT = 1  //Constant for  sent message view type
        private const val VIEW_TYPE_RECEIVED = 2  //Cosntant for received message view type
    }
}
