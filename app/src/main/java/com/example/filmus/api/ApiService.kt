package com.example.filmus.api

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.Response as AuthResponse


data class LoginRequest(
    @Json(name = "username") val username: String, @Json(name = "password") val password: String
)

data class RegistrationRequest(
    @Json(name = "username") val login: String,
    @Json(name = "name") val name: String,
    @Json(name = "password") val password: String,
    @Json(name = "email") val email: String,
    @Json(name = "birthDate") val birthDate: String,
    @Json(name = "gender") val gender: Int
)

data class LoginResponse(
    @Json(name = "token") val token: String
)

data class RegistrationResponse(
    @Json(name = "token") val token: String
)

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): AuthResponse {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder().header("Authorization", "Bearer $token")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

interface ApiService {
    @POST("/api/account/login")
    suspend fun login(@Body loginRequest: RequestBody): Response<LoginResponse>

    @POST("/api/account/register")
    suspend fun register(@Body registerRequest: RequestBody): Response<RegistrationResponse>
}


fun createApiService(token: String? = null): ApiService {
    val okHttpClient = OkHttpClient.Builder()
    if (!token.isNullOrBlank()) {
        okHttpClient.addInterceptor(AuthInterceptor(token))
    }

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // todo вынести в константу
    val retrofit = Retrofit.Builder().baseUrl("https://react-midterm.kreosoft.space")
        .client(okHttpClient.build()).addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    return retrofit.create(ApiService::class.java)
}


