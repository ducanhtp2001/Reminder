package com.example.workreminder.data.network.reponsemodel

import com.example.workreminder.data.network.model.User

class LoginResponse(val user: User?, val status: Boolean, val message: String) {
}