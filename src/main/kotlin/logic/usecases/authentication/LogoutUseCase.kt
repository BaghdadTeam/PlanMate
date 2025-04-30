package org.baghdad.logic.usecases.authentication

import org.baghdad.logic.repositories.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepo: AuthenticationRepository
)
{
    operator fun invoke() {
        TODO()
    }
}