package com.example.filmus.domain.api

sealed class ApiResult<out T> {
    data class Success<T>(val data: T? = null) : ApiResult<T>()
    data class Unauthorized(val message: String? = null) : ApiResult<Nothing>()
    data class Error(val message: String? = null, val code: Int? = null) : ApiResult<Nothing>()
}
