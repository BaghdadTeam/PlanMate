package logic.usecase.authentication

import helpers.authentication.SessionTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.usecase.authentication.CleanExpiredSessionUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CleanExpiredSessionUseCaseTest {
    lateinit var sessionRepository: SessionRepository
    lateinit var useCase: CleanExpiredSessionUseCase

    @BeforeEach
    fun setUp() {
        sessionRepository = mockk(relaxed = true)
        useCase = CleanExpiredSessionUseCase(sessionRepository)
    }
    @Test
    fun `Should delete all expired sessions`() {
        val oldLogin = LocalDateTime.now().minusMinutes(45)
        // Given
        every { sessionRepository.loadSession() }returns SessionEntity(
            userId = "user123",
            loginTime = oldLogin,
            token = "token"
        )
        // When
        useCase.invoke()
        // Then
        verify { sessionRepository.deleteSession() }
    }
    @Test
    fun `Should not delete a session if it does not expired yet`(){
        every { sessionRepository.loadSession() } returns SessionTestData.baseSession
        useCase.invoke()
        verify (exactly = 0){ sessionRepository.deleteSession() }
    }

    @Test
    fun `Should not do a thing if there is no session`() {
        // Given
        every { sessionRepository.loadSession() } returns null
        // When
        useCase.invoke()
        // Then
        verify(exactly = 0) { sessionRepository.deleteSession() }
    }

}