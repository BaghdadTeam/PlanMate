package org.baghdad.logic.usecases.authentication

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.SessionRepository

class LoadSessionUseCase(
    private val sessionRepository: SessionRepository)
{
    operator fun invoke(): Result<SessionEntity>
    {
        TODO()
    }
}