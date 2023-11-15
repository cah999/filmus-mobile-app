package com.example.filmus.repository.registration

import com.example.filmus.domain.registration.register.RegistrationRequest
import com.example.filmus.domain.registration.register.RegistrationResult

interface IRegistrationRepository {
    suspend fun register(data: RegistrationRequest): RegistrationResult
}