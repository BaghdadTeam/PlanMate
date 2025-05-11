package org.baghdad.logic.usecase.authentication

import org.baghdad.logic.repositories.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepo: AuthenticationRepository
)
{
    suspend operator fun invoke() {
        authenticationRepo.logout()
    }
}