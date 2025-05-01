package org.baghdad.logic.usecase.authentication

import org.baghdad.logic.repositories.SessionRepository

class CleanExpiredSessionUseCase(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke() {
        val session = sessionRepository.loadSession()
        if (session != null && session.isExpired())
            sessionRepository.deleteSession()
    }
}