import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.meetingscheduler.ViewModels.CalendarviewModel
import com.meetingscheduler.adapters.CalendarAdapter
import com.meetingscheduler.databinding.FragmentHomeBinding

class Home : Fragment() {

    private lateinit var calendarViewModel: CalendarviewModel
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        calendarViewModel = ViewModelProvider(this).get(CalendarviewModel::class.java)

        // Configure the RecyclerView
        calendarAdapter = CalendarAdapter(emptyList()) { day ->
            // Handle day click here
            if (day.hasEvent) {
                // Show event details, etc.
            }
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 7)  // 7 columns for 7 days of the week
            adapter = calendarAdapter
        }

        // Observe LiveData from the ViewModel
        calendarViewModel.days.observe(viewLifecycleOwner, Observer { days ->
            calendarAdapter.updateDays(days)  // Update the adapter with the new days
        })

        // Generate days for the current month
        calendarViewModel.genererDaysForMonth()

        return binding.root
    }
}
