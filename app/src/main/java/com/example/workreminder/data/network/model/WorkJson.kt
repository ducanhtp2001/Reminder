package com.example.workreminder.data.network.model

class WorkForJson(
    val title: String,
    val description: String,
    val updateTime: String,
    val targetTime: String,
    val isUnable: Boolean,
    val userId: Int
) {}