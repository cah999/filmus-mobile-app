package com.example.filmus.domain.registration.register

import com.example.filmus.repository.registration.RegistrationRepository


class RegistrationUseCase(private val registrationRepository: RegistrationRepository) {
    suspend fun register(request: RegistrationRequest): RegistrationResult {
        return registrationRepository.register(request)
    }
}