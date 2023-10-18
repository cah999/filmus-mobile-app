package com.example.filmus.api

import android.util.Log
import com.example.filmus.domain.model.LoginResult
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.converter.moshi.MoshiConverterFactory


data class LoginRequest(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String
)

data class LoginResponse(
    @Json(name = "token") val token: String
)

interface ApiService {
    @POST("/api/account/login")
    suspend fun login(@Body loginRequest: RequestBody): Response<LoginResponse>
}


fun createApiService(): ApiService {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://react-midterm.kreosoft.space")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    return retrofit.create(ApiService::class.java)
}


