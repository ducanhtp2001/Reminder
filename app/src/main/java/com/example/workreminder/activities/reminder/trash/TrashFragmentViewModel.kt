package com.example.workreminder.activities.reminder.trash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashFragmentViewModel @Inject constructor(val repository: Repository): ViewModel() {
    var works: LiveData<List<WorkEntity>> = MutableLiveData()

    fun getLocalData(user: User) = viewModelScope.launch {
        works = repository.getAllLocalData(userId = user.id!!)
    }

    fun deleteLocalData(work: WorkEntity) = viewModelScope.launch {
        repository.deleteLocalData(listOf(work))
    }
}