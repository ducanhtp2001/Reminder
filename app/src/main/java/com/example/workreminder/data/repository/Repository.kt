package com.example.workreminder.data.repository

import android.util.Log
import com.example.workreminder.data.local.dao.WorkDao
import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.data.network.apiservice.ApiService
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.network.model.WorkForJson
import com.example.workreminder.data.network.reponsemodel.LoginResponse
import com.example.workreminder.data.network.reponsemodel.UpdateResponse
import com.example.workreminder.usecase.UserSharePreference
import javax.inject.Inject

class Repository @Inject constructor(
    private val dao: WorkDao,
    private val apiService: ApiService,
    private val sharePreference: UserSharePreference
) {
    // local data
    fun getAllLocalData(userId: Int) = dao.getWorksByUserId(userId)
    suspend fun updateLocalData(workEntitys: List<WorkEntity>) = dao.updateWork(*workEntitys.toTypedArray())
    suspend fun deleteLocalData(workEntitys: List<WorkEntity>) = dao.deleteWork(*workEntitys.toTypedArray())
    suspend fun insertLocalData(workEntitys: List<WorkEntity>) {
        dao.insertWork(*workEntitys.toTypedArray())
        Log.d("InsertLocalData", "Inserted ${workEntitys.size} records into local database")
    }

    // network data
    suspend fun getAllNetworkData(userId: Int) = apiService.getAllWork(userId)
    suspend fun updateNetworkData(work: WorkEntity) = apiService.updateWork(work)
    suspend fun deleteNetworkData(work: WorkEntity) = apiService.deleteWork(work)
    suspend fun insertNetworkData(workJson: WorkForJson) = apiService.insertWork(workJson)

    // login
    suspend fun login(user: User): LoginResponse {
        return apiService.verifyAccount(user)
    }
    suspend fun register(user: User): UpdateResponse? {
        return apiService.register(user)
    }
    fun getSharePreference() = sharePreference.getSharePreference()
}