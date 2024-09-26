package com.meetingscheduler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.meetingscheduler.ViewModels.NotificationViewModel
import com.meetingscheduler.adapters.NotificationAdapter
import com.meetingscheduler.databinding.FragmentNotificationBinding


class Notification : Fragment() {
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentNotificationBinding.inflate(inflater, container, false)
        //Intialisation de notification view model
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        // Intialisation  de l'adapter
        notificationAdapter = NotificationAdapter(emptyList())
        // Configuration de  l'adapter
        binding.rvNotification.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter

        }
        notificationViewModel.notification.observe(viewLifecycleOwner, Observer { notifications ->
            notificationAdapter.updateNotifications(notifications)
        })

        return binding.root
    }
}