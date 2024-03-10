package com.example.workreminder.activities.reminder.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.data.network.model.WorkForJson
import com.example.workreminder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFragmentViewModel @Inject constructor(val repository: Repository) : ViewModel(){
    val addWorkPart: MutableLiveData<Int> = MutableLiveData()
    val isGetRequest: MutableLiveData<Boolean> = MutableLiveData()
    val isGetSuccess: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isGetRequest.value = false
        isGetSuccess.value = false
    }

    init {
        addWorkPart.value = 1
    }
    fun nextPart() {
        addWorkPart.value = addWorkPart.value?.plus(1)
    }

    fun prePart() {
        addWorkPart.value = addWorkPart.value?.minus(1)
    }

    fun insertWork(workForJson: WorkForJson) = viewModelScope.launch {
        isGetRequest.value = true
        val response = repository.insertNetworkData(workForJson)
        if (response != null && response.status) {
            isGetRequest.value = false
            isGetSuccess.value = true
        } else isGetRequest.value = false
    }
}