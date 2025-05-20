package logic.usecase.authentication

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.usecase.authentication.LogoutUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LogoutUseCaseTest {
    private lateinit var useCase: LogoutUseCase
    private lateinit var authRepo: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authRepo = mockk(relaxed = true)
        useCase = LogoutUseCase(authRepo)
    }

    @Test
    fun `Should invoke authRep logout function as the invoke called `() = runTest {
        // Given

        // When
        useCase.invoke()
        // Then
        coVerify { authRepo.logout() }
    }

    @Test
    fun `Should re throw exceptions from auth repo `() = runTest {
        // Given
        coEvery { authRepo.logout() } throws LogoutFailedException("logout failed")
        // When & Then
        assertThrows<LogoutFailedException> { useCase.invoke() }
    }
}