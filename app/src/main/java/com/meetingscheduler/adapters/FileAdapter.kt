package com.meetingscheduler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.Model.File
import com.meetingscheduler.R

class FileAdapter(private var files: List<File>) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon_file = itemView.findViewById<ImageView>(R.id.file_icon)
        private val name_file = itemView.findViewById<TextView>(R.id.file_name)
        fun bind(file: File) {
            when (file.type_file) {
                "pdf" -> icon_file.setImageResource(R.drawable.pdf)
                "word" -> icon_file.setImageResource(R.drawable.file_texte)
                "xls" -> icon_file.setImageResource(R.drawable.pdf)
                else -> icon_file.setImageResource(R.drawable.pdf)
            }
            name_file.text = file.name_file
        }
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun getItemCount() = files.size
    fun updateFile(newFiles: List<File>) {
        files = newFiles
        notifyDataSetChanged()
    }

}