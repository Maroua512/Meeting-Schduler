package com.meetingscheduler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meetingscheduler.Model.Day
import com.meetingscheduler.R
import com.meetingscheduler.ViewModels.CalendarviewModel
import java.util.Calendar

class CalendarAdapter(
    private var days: List<Day>,private val onDayClicked:(Day)->Unit

) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.tvDayNumber)
        val eventIndicatorContainer: LinearLayout = itemView.findViewById(R.id.eventIndicatorContainer)
        val indicatorMeeting: View = itemView.findViewById(R.id.indicatorMeeting)
        val indicatorAssignment: View = itemView.findViewById(R.id.indicatorAssignment)
        val indicatorInfo: View = itemView.findViewById(R.id.indicatorInfo)
        fun bind(day: Day) {
            dayTextView.text = day.dayNumber.toString()

            if (day.isToday) {
                dayTextView.setBackgroundResource(R.drawable.day_circle_bg)
            } else {
                dayTextView.setBackgroundColor(0)
            }

            if (day.hasEvent) {
                eventIndicatorContainer.visibility = View.VISIBLE
                indicatorMeeting.visibility = if (day.hasMeeting) View.VISIBLE else View.GONE
                indicatorAssignment.visibility = if (day.hasAssignment) View.VISIBLE else View.GONE
                indicatorInfo.visibility = if (day.hasInfo) View.VISIBLE else View.GONE
            } else {
                eventIndicatorContainer.visibility = View.GONE
            }
           itemView.setOnClickListener {
               onDayClicked(day)
           }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size

    // Méthode pour mettre à jour les jours
    fun updateDays(newDays: List<Day>) {
        days = newDays
        notifyDataSetChanged()
    }


}
