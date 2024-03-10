package com.example.workreminder.activities.reminder

import androidx.lifecycle.ViewModel
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val repository: Repository): ViewModel() {
    internal var user: User? = null
}