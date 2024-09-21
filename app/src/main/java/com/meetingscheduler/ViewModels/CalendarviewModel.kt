package com.meetingscheduler.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meetingscheduler.Model.Day
import java.util.Calendar

class CalendarviewModel:ViewModel() {
    // LiveData qui contient la liste des jours
    private val _days  = MutableLiveData<List<Day>>()
    val  days:LiveData<List<Day>> get() = _days
    fun genererDaysForMonth(){
        val days = mutableListOf<Day>()
        val calendar = Calendar.getInstance()
        for (i in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            val isToday = (i == calendar.get(Calendar.DAY_OF_MONTH))
            val hasMeeting = (i % 3 == 0)  // Exemple: Réunion tous les 3 jours
            val hasAssignment = (i % 5 == 0)  // Exemple: Devoir tous les 5 jours
            val hasInfo = (i % 7 == 0)  // Exemple: Information tous les 7 jours
            days.add(Day(dayNumber = i, isToday = isToday, hasEvent = hasMeeting || hasAssignment || hasInfo,
                hasMeeting = hasMeeting, hasAssignment = hasAssignment, hasInfo = hasInfo))
        }
       _days.value = days
    }

}