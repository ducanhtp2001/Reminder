package com.example.workreminder.activities.login.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.repository.Repository
import com.example.workreminder.usecase.JsonAdapter
import com.example.workreminder.usecase.UserSharePreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    internal var isLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    internal var isLoginRequest: MutableLiveData<Boolean> = MutableLiveData()
    internal var isStart: Boolean = true
    internal var message: String = ""
    internal var localUser: User? = null

    init {
        isLoginSuccess.value = false
        isLoginRequest.value = false
    }

    internal fun getSharePreference() = repository.getSharePreference()

    internal fun login(user: User, isRemember: Boolean) = viewModelScope.launch {
        // visible progress bar
        isLoginRequest.value = true

        isStart = false
        val response = repository.login(user)
        message = response.message
        if (response.status) {
            // hind progress bar
            isLoginRequest.value = false


            localUser = response.user

            // save to sharePreference
            if (isRemember) {
                if (response.user != null) {
                    val userOnResponse = response.user
                    val userJson = JsonAdapter<User>().toJson(userOnResponse!!)

                    repository.getSharePreference()
                        .edit()
                        .putString(UserSharePreference.SHARE_NAME, userJson)
                        .apply()
                } else {
                    Log.e("testing", "login false")
                }
            } else {
                repository.getSharePreference()
                    .edit()
                    .remove(UserSharePreference.SHARE_NAME)
                    .apply()
            }

            isLoginSuccess.value = true
        } else {
            isLoginRequest.value = false
        }
    }
}