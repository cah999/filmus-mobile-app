package com.example.filmus.repository.registration

import com.example.filmus.api.RegistrationRequest
import com.example.filmus.domain.registration.RegistrationResult


interface RegistrationRepository {
    suspend fun register(data: RegistrationRequest): RegistrationResult
}
