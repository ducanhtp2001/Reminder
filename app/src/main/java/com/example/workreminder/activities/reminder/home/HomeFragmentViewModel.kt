package com.example.workreminder.activities.reminder.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val isGetRequest: MutableLiveData<Boolean> = MutableLiveData()
    val works: MutableLiveData<List<WorkEntity>> = MutableLiveData()

    init {
        isGetRequest.value = false
        works.value = listOf()
    }

    fun updateWork(workEntity: WorkEntity) = viewModelScope.launch {
        isGetRequest.value = true
        val response = repository.updateNetworkData(workEntity)
        if (response != null && response.status) {
            // update home screen
        }
        isGetRequest.value = false
    }

    fun deleteWork(workEntity: WorkEntity, user: User) = viewModelScope.launch {
        isGetRequest.value = true
        val response = repository.deleteNetworkData(workEntity)
        repository.insertLocalData(listOf(workEntity))
        if (response != null && response.status) {
            getAllWork(user)
        }
        isGetRequest.value = false
    }

    fun getAllWork(user: User) = viewModelScope.launch {

        isGetRequest.value = true
        val response = repository.getAllNetworkData(user.id!!) // user.id!!
        if (response != null && response.status) {
            // update home screen
            isGetRequest.value = false
            works.value = response.works
        } else {
            isGetRequest.value = false
        }
    }

    fun search(s: String): List<WorkEntity> {
        return works.value?.filter { it.title.contains(s, true) } ?: emptyList()
    }

}