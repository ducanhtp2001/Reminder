package com.example.workreminder.data.network.reponsemodel

import com.example.workreminder.data.local.model.WorkEntity

class WorkResponse(val works: List<WorkEntity>?, val status: Boolean, val message: String) {
}