
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.meetingscheduler.Model.Day

import com.meetingscheduler.Utils.DatabseHelper
import com.meetingscheduler.ViewModels.CalendarviewModel
import com.meetingscheduler.adapters.CalendarAdapter
import com.meetingscheduler.adapters.EventAdapter
import com.meetingscheduler.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar


class Home : Fragment() {

    private lateinit var calendarViewModel: CalendarviewModel
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        calendarViewModel = ViewModelProvider(this).get(CalendarviewModel::class.java)



        // Configure the RecyclerView

            calendarAdapter = CalendarAdapter(emptyList()){day ->onDayClicked(day) }
            eventAdapter = EventAdapter(emptyList())
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, 7)  // 7 columns for 7 days of the week
                adapter = calendarAdapter
            }

            //Intialiser  Event  Adapter
            binding.listEvent.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = eventAdapter
            }
            // Observe LiveData from the ViewModel
            calendarViewModel.days.observe(viewLifecycleOwner, Observer { days ->
                calendarAdapter.updateDays(days)  // Update the adapter with the new days
            })
            // Observe LiveData from the ViewModel
            calendarViewModel.toDayEvent.observe(viewLifecycleOwner, Observer { toDayEvents ->
                eventAdapter.updateEvents(toDayEvents)  // Update the adapter with the new days
            })

            // Generate days for the current month
            calendarViewModel.genererDaysForMonth()

            return binding.root
        }
    private fun onDayClicked(day: Day) {
        Toast.makeText(requireContext(), "Clic sur le jour ${day.hasEvent}", Toast.LENGTH_SHORT).show()
        if (day.hasEvent) {
            // Mettre à jour les événements du jour sélectionné
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, day.dayNumber)
            val currentDayString = calendarViewModel.getJour(calendar.time)

            val eventsForDay = calendarViewModel.events.value?.filter { event ->
                Toast.makeText(requireContext(), "Clic1 sur le jour : ${event.jour_event}", Toast.LENGTH_SHORT).show()
                event.jour_event == currentDayString
            }

            if (!eventsForDay.isNullOrEmpty()) {
                eventAdapter.updateEvents(eventsForDay)
            } else {
                eventAdapter.updateEvents(emptyList())  // Aucun événement pour ce jour
            }
        } else {
            eventAdapter.updateEvents(emptyList())  // Si aucun événement pour ce jour
        }
    }
    }

