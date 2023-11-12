package com.example.filmus.domain.api

import com.example.filmus.common.Constants
import com.example.filmus.domain.api.interceptor.AuthInterceptor
import com.example.filmus.domain.favorite.FavoritesResponse
import com.example.filmus.domain.login.LoginResponse
import com.example.filmus.domain.main.MoviesResponse
import com.example.filmus.domain.movie.DetailedMovieResponse
import com.example.filmus.domain.profile.ProfileResponse
import com.example.filmus.domain.registration.register.RegistrationResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


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

    @GET("/api/favorites")
    suspend fun getFavorites(): Response<FavoritesResponse>

    @POST("/api/favorites/{id}/add")
    suspend fun addFavorite(@Path("id") id: String): Response<Unit>

    @DELETE("/api/favorites/{id}/delete")
    suspend fun removeFavorite(@Path("id") id: String): Response<Unit>

    @POST("/api/movie/{movieID}/review/add")
    suspend fun addReview(
        @Path("movieID") movieID: String,
        @Body reviewRequest: RequestBody
    ): Response<Unit>

    @PUT("/api/movie/{movieID}/review/{reviewID}/edit")
    suspend fun editReview(
        @Path("movieID") movieID: String,
        @Path("reviewID") reviewID: String,
        @Body reviewRequest: RequestBody
    ): Response<Unit>


    @DELETE("/api/movie/{movieID}/review/{reviewID}/delete")
    suspend fun removeReview(
        @Path("movieID") movieID: String,
        @Path("reviewID") reviewID: String,
    ): Response<Unit>

}


fun createApiService(token: String? = null): ApiService {
    val okHttpClient = OkHttpClient.Builder()
    if (!token.isNullOrBlank()) {
        okHttpClient.addInterceptor(AuthInterceptor(token))
    }

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val retrofit = Retrofit.Builder().baseUrl(Constants.API_URL)
        .client(okHttpClient.build()).addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    return retrofit.create(ApiService::class.java)
}


