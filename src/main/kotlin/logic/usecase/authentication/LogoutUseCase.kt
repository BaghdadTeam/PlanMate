package org.baghdad.logic.usecase.authentication

import org.baghdad.logic.repositories.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepo: AuthenticationRepository
)
{
    operator fun invoke() {
        authenticationRepo.logout()
    }
}