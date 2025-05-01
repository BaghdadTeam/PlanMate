package org.baghdad.logic.usecase.authentication

import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionEndedException
import org.baghdad.logic.model.exceptions.SessionNotFoundException
import org.baghdad.logic.repositories.SessionRepository

class GetActiveSessionUseCase(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(): Result<SessionEntity> {
        val session = sessionRepository.loadSession()
            ?: return Result.failure(SessionNotFoundException("Session not found"))
        if (session.isExpired())
            return Result.failure(SessionEndedException("Session is expired"))
        return Result.success(session)
    }
}