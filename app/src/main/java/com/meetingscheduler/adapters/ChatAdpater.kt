package com.meetingscheduler.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.meetingscheduler.Model.D_Friend
import com.marouasapplication.app.modules.discution.ui.DiscutionActivity
import com.meetingscheduler.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.Int

class ChatAdpater: RecyclerView.Adapter<ChatAdpater.ViewHolder>() {

  var items: MutableList<D_Friend> = mutableListOf()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
    return ViewHolder(itemView)
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val prestataire = items[position]
    holder.bind(prestataire)
  }

  inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val ivFriend: ShapeableImageView = itemView.findViewById(R.id.ivFriend)
    val tvName: TextView = itemView.findViewById(R.id.tvName)
    val tvLastMsg: TextView = itemView.findViewById(R.id.tvLastMsg)
    val tvHour: TextView = itemView.findViewById(R.id.tvHour)

    fun bind(friend: D_Friend){
      tvName.text = friend.name
      tvLastMsg.text = friend.lastMsg

      // formater de Long a text
      val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
      tvHour.text = sdf.format(Date(friend.timestamp))

      Glide.with(itemView.context).load(friend.image).placeholder(R.drawable.img_lock).into(ivFriend)

      itemView.setOnClickListener {
        Intent(itemView.context, DiscutionActivity::class.java).also {
          itemView.context.startActivity(it)
        }
      }
    }
  }

}
