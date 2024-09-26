package com.meetingscheduler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.meetingscheduler.Model.Notification
import com.meetingscheduler.R

class NotificationAdapter(private var notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>(

    ) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titre: TextView = itemView.findViewById(R.id.titre)
        val description: TextView = itemView.findViewById(R.id.description)
        val date: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.imageProfile)

        fun bind(notification: Notification) {
            titre.text = notification.titre_notification
            description.text = notification.description_notification
            date.text = notification.date_notification
            Glide.with(itemView.context).load(notification.image_notification)
                .placeholder(R.drawable.profil_vide).into(image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount() = notifications.size
    fun updateNotifications(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }
}


