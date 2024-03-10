package com.example.workreminder.data.network.apiservice


import com.example.workreminder.data.local.model.WorkEntity
import com.example.workreminder.data.network.reponsemodel.LoginResponse
import com.example.workreminder.data.network.reponsemodel.UpdateResponse
import com.example.workreminder.data.network.model.User
import com.example.workreminder.data.network.model.WorkForJson
import com.example.workreminder.data.network.reponsemodel.WorkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("user-login-api.php")
    suspend fun verifyAccount(@Body user: User): LoginResponse

    @POST("user-register-api.php")
    suspend fun register(@Body user: User): UpdateResponse?

    @GET("get-work-api.php")
    suspend fun getAllWork(@Query("userId") userId: Int): WorkResponse?

    @POST("update-work-api.php")
    suspend fun updateWork(@Body work: WorkEntity): UpdateResponse?

    @POST("insert-work-api.php")
    suspend fun insertWork(@Body work: WorkForJson): UpdateResponse?

    @POST("delete-work-api.php")
    suspend fun deleteWork(@Body work: WorkEntity): UpdateResponse?
}