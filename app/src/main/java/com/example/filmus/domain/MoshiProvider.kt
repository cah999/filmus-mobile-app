package com.example.filmus.domain

import com.example.filmus.api.LoginRequest
import com.example.filmus.api.ProfileResponse
import com.example.filmus.api.RegistrationRequest
import com.example.filmus.api.ReviewRequest
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
    val profileRequestAdapter: JsonAdapter<ProfileResponse> =
        moshi.adapter(ProfileResponse::class.java)
    val reviewRequestAdapter: JsonAdapter<ReviewRequest> =
        moshi.adapter(ReviewRequest::class.java)
}