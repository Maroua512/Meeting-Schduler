package com.meetingscheduler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.Model.D_Message
import com.meetingscheduler.R

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiscutionAdapter(
  private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  var items: MutableList<D_Message> = mutableListOf()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  companion object {
    private const val VIEW_TYPE_SENT = 1
    private const val VIEW_TYPE_RECEIVED = 2
  }

  override fun getItemViewType(position: Int): Int {
    val message = items[position]
    return if (message.sender == currentUserId) {
      VIEW_TYPE_SENT
    } else {
      VIEW_TYPE_RECEIVED
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == VIEW_TYPE_SENT) {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_chat_right, parent, false)
      SentMessageViewHolder(view)
    } else {
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_chat_left, parent, false)
      ReceivedMessageViewHolder(view)
    }
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val message = items[position]
    if (holder is SentMessageViewHolder) {
      holder.bind(message)
    } else if (holder is ReceivedMessageViewHolder) {
      holder.bind(message)
    }
  }

  class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textViewMessage: TextView = view.findViewById(R.id.tvMsg)
    private val tvHour: TextView = view.findViewById(R.id.tvHour)

    fun bind(message: D_Message) {
      textViewMessage.text = message.texte
      val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
      tvHour.text = sdf.format(Date(message.timestamp))
    }
  }

  class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textViewMessage: TextView = view.findViewById(R.id.tvMsg)
    private val tvHour: TextView = view.findViewById(R.id.tvHour)

    fun bind(message: D_Message) {
      textViewMessage.text = message.texte
      val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
      tvHour.text = sdf.format(Date(message.timestamp))
    }
  }
}
