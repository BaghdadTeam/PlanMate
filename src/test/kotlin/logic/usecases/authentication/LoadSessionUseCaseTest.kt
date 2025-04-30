package logic.usecases.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.SessionTestData
import io.mockk.every
import io.mockk.mockk
import org.baghdad.logic.model.entities.SessionEntity
import org.baghdad.logic.model.exceptions.SessionEndedException
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.usecases.authentication.LoadSessionUseCase
import org.junit.jupiter.api.*

class LoadSessionUseCaseTest {
    private lateinit var useCase: LoadSessionUseCase
    private lateinit var repository: SessionRepository

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = LoadSessionUseCase(repository)
    }

    @Test
    fun `should return success result with SessionEntity when session there is a session exists`() {
        // When
        every { repository.loadSession() } returns SessionTestData.baseSession
        // Given
        val result = useCase.invoke()
        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should return failure result  there is no existing session`() {
        // Given
        every { repository.loadSession() } returns null
        // When
        val result = useCase.invoke()
        // Then
        assertThat(result.isFailure).isTrue()
    }
    @Test
    fun `Should throw session ended exception if session is expired`() {
        // Given
        every { repository.loadSession() } returns null
        // When & Then
        assertThrows <SessionEndedException>{ useCase.invoke() }
    }
}