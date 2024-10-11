package com.meetingscheduler.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.meetingscheduler.Model.Discussion
import com.meetingscheduler.R

/**
 * Adapter class  for  dispalying Chat in recylerView
 */
class ChatAdpater : RecyclerView.Adapter<ChatAdpater.ViewHolder>() {

    //List of Friends to be displaying in recylcerView
    var items: MutableList<Discussion> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()  // Notify the adapter that the data has changed and the UI needs to be updated
        }

    /**
     *  ViewHolder class to represent each discussion item in recyclerView
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivFriend: ShapeableImageView = itemView.findViewById(R.id.ivFriend)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvLastMsg: TextView = itemView.findViewById(R.id.tvLastMsg)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)

        fun bind(discussion: Discussion) {
            tvName.text = discussion.disName
            tvLastMsg.text = discussion.lastMessage
            tvHour.text = discussion.disHour
            Glide.with(itemView.context).load(discussion.disImage).placeholder(R.drawable.profil_vide)
                .into(ivFriend)

            itemView.setOnClickListener {
                Intent(itemView.context, com.meetingscheduler.ui.Discussion::class.java).also {
                    it.putExtra("discussion", discussion)
                    itemView.context.startActivity(it)
                }
            }
        }
    }

    /**
     *  Inflate the layout for each item in recyclerView
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(itemView)
    }

    /**
     *  Return the number of discussion in items(list )
     */
    override fun getItemCount() = items.size

    /**
     *  Create the ViewHolder for each item in recyclerView
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val discussion = items[position]
        holder.bind(discussion)
    }

    /**
     * Update the  discussion in the adapter and  refresh the UI
     */
   fun updateDiscussions(newDiscussions: MutableList<Discussion>) {
        items = newDiscussions
        notifyDataSetChanged() // Notify the adapter to update the recyclerview
    }
}
