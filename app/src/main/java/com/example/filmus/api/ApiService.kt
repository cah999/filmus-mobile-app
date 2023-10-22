package com.example.filmus.api

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String
)

data class RegistrationRequest(
    @Json(name = "username") val login: String,
    @Json(name = "name") val name: String,
    @Json(name = "password") val password: String,
    @Json(name = "email") val email: String,
    @Json(name = "birthDate") val birthDate: String,
    @Json(name = "gender") val gender: Boolean
)

data class LoginResponse(
    @Json(name = "token") val token: String
)

data class RegistrationResponse(
    @Json(name = "token") val token: String
)

//data class RegistrationResponse(
//    @Json(name = "message") val message: String,
//    @Json(name = "errors") val errors: String,
//    @Json(name = "token") val token: String
//)

interface ApiService {
    @POST("/api/account/login")
    suspend fun login(@Body loginRequest: RequestBody): Response<LoginResponse>

    @POST("/api/account/register")
    suspend fun register(@Body registerRequest: RequestBody): Response<RegistrationResponse>
}


fun createApiService(): ApiService {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // todo вынести в константу
    val retrofit = Retrofit.Builder()
        .baseUrl("https://react-midterm.kreosoft.space")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    return retrofit.create(ApiService::class.java)
}


