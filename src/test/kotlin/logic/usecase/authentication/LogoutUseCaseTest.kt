package logic.usecase.authentication

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `Should invoke authRep logout function as the invoke called `(){
        // Given

        // When
        useCase.invoke()
        // Then
        verify { authRepo.logout() }

    }

    @Test
    fun `Should re throw exceptions from auth repo `(){
        // Given
        every {authRepo.logout()} returns Result.failure(LogoutFailedException("logout failed"))
        // When
        assertThrows<LogoutFailedException> { useCase.invoke() }
    }
}