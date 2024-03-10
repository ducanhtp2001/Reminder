package com.example.workreminder.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workreminder.data.local.dao.WorkDao
import com.example.workreminder.data.local.model.WorkEntity

@Database(entities = [WorkEntity::class], version = 1)
abstract class WorkDatabase: RoomDatabase(){
    abstract fun getWorkDao(): WorkDao
}