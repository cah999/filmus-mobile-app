package com.example.filmus.api

import com.example.filmus.domain.database.ProfileEntity
import com.example.filmus.domain.main.Movie
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
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

data class MoviesResponse(
    @Json(name = "movies") val results: List<Movie>
)

data class DetailedMovieResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?,
    @Json(name = "poster") val poster: String?,
    @Json(name = "year") val year: Int,
    @Json(name = "country") val country: String?,
    @Json(name = "genres") val genres: List<GenreResponse?>,
    @Json(name = "reviews") val reviews: List<ReviewResponse?>,
    @Json(name = "time") val time: Int,
    @Json(name = "tagline") val tagline: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "director") val director: String?,
    @Json(name = "budget") val budget: Int?,
    @Json(name = "fees") val fees: Int?,
    @Json(name = "ageLimit") val ageLimit: Int
)

data class GenreResponse(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String?
)

data class ReviewResponse(
    @Json(name = "id") val id: String,
    @Json(name = "rating") val rating: Int,
    @Json(name = "reviewText") val reviewText: String?,
    @Json(name = "isAnonymous") val isAnonymous: Boolean,
    @Json(name = "createDateTime") val createDateTime: String,
    @Json(name = "author") val author: AuthorResponse?
)

data class AuthorResponse(
    @Json(name = "userId") val userId: String,
    @Json(name = "nickName") val nickName: String?,
    @Json(name = "avatar") val avatar: String?
)

data class ProfileResponse(
    @Json(name = "id") val id: String,
    @Json(name = "nickName") val nickname: String,
    @Json(name = "email") val email: String,
    @Json(name = "avatarLink") val avatarLink: String?,
    @Json(name = "name") val name: String,
    @Json(name = "birthDate") val birthDate: String,
    @Json(name = "gender") val gender: Int
)

fun ProfileResponse.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        nickname = this.nickname,
        email = this.email,
        avatarLink = if (this.avatarLink.isNullOrBlank()) "" else this.avatarLink,
        name = this.name,
        gender = this.gender,
        birthDate = this.birthDate
    )
}

sealed class ProfileResult {
    data class Success(val profile: ProfileResponse) : ProfileResult()
    data class Unauthorized(val message: String) : ProfileResult()
    data class Error(val message: String) : ProfileResult()
}


sealed class ProfileUpdateResult {
    data class Success(val message: String) : ProfileUpdateResult()
    data class Unauthorized(val message: String) : ProfileUpdateResult()
    data class Error(val message: String) : ProfileUpdateResult()
}

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

    @POST("/api/account/logout")
    suspend fun logout(): Response<Unit>

    @GET("/api/movies/{page}")
    suspend fun getMovies(@Path("page") page: Int): Response<MoviesResponse>

    @GET("/api/account/profile")
    suspend fun getInfo(): Response<ProfileResponse>

    @PUT("/api/account/profile")
    suspend fun updateInfo(@Body profileRequest: RequestBody): Response<Unit>

    @GET("/api/movies/details/{id}")
    suspend fun getDetailedMovie(@Path("id") id: String): Response<DetailedMovieResponse>
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


