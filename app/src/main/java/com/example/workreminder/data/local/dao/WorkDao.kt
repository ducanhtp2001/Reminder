package com.example.workreminder.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.workreminder.data.local.model.WorkEntity

@Dao
interface WorkDao {
    @Query("SELECT * FROM works WHERE userid = :userId")
    fun getWorksByUserId(userId: Int): LiveData<List<WorkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWork(vararg workEntity: WorkEntity)

    @Update
    suspend fun updateWork(vararg workEntity: WorkEntity)

    @Delete
    suspend fun deleteWork(vararg workEntity: WorkEntity)
}