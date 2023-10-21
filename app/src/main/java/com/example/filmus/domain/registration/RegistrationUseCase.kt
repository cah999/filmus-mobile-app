package com.example.filmus.domain.registration

import com.example.filmus.api.RegistrationRequest
import com.example.filmus.repository.registration.RegistrationRepository

class RegistrationUseCase(private val registrationRepository: RegistrationRepository) {
    suspend fun register(request: RegistrationRequest): RegistrationResult {
        return registrationRepository.register(request)
    }
}