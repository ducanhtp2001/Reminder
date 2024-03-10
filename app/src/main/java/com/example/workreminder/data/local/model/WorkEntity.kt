package com.example.workreminder.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "works")
open class WorkEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var title: String,
    var description: String,
    var updateTime: String,
    var targetTime: String,
    var isUnable: Boolean,
    var userid: Int
)