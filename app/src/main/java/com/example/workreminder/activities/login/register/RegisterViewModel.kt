package com.example.workreminder.activities.login.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val repository: Repository): ViewModel(){
    var isRegisterSuccess: MutableLiveData<Boolean> = MutableLiveData()
    var isRegisterRequest: MutableLiveData<Boolean> = MutableLiveData()
    var isStart: Boolean = true
    var message: String = ""

    init {
        isRegisterSuccess.value = false
        isRegisterRequest.value = false
    }

    fun register(user: User) = viewModelScope.launch {
        isRegisterRequest.value = true
        isStart = false
        val response = repository.register(user)
        if (response != null && response.status) {
            message = response.message
            isRegisterSuccess.value = true
        } else {
            message = response?.message ?: "False"
            isRegisterRequest.value = false
        }
    }
}