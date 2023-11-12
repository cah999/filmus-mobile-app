package com.example.filmus.domain

import com.example.filmus.domain.login.LoginRequest
import com.example.filmus.domain.movie.ReviewRequest
import com.example.filmus.domain.profile.ProfileResponse
import com.example.filmus.domain.registration.register.RegistrationRequest
import com.example.filmus.domain.registration.register.error.RegistrationErrorDetails
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiProvider {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val loginRequestAdapter: JsonAdapter<LoginRequest> = moshi.adapter(LoginRequest::class.java)
    val registerRequestAdapter: JsonAdapter<RegistrationRequest> =
        moshi.adapter(RegistrationRequest::class.java)
    val registrationErrorAdapter: JsonAdapter<RegistrationErrorDetails> =
        moshi.adapter(RegistrationErrorDetails::class.java)
    val profileRequestAdapter: JsonAdapter<ProfileResponse> =
        moshi.adapter(ProfileResponse::class.java)
    val reviewRequestAdapter: JsonAdapter<ReviewRequest> =
        moshi.adapter(ReviewRequest::class.java)
}