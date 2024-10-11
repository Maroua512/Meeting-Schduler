package com.meetingscheduler.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListOfDiscussionsFactory(private val userViewModel: UserViewModel):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListOfDiscussions::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListOfDiscussions(userViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}