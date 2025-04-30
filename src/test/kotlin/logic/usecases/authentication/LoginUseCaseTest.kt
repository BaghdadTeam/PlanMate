package logic.usecases.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.usecases.authentication.LoginUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginUseCaseTest {
    private lateinit var useCase: LoginUseCase
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var sessionRepository: SessionRepository

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        sessionRepository = mockk()
        useCase = LoginUseCase(authRepository, sessionRepository)
    }

    @Test
    fun `Should return success result when login success`() {
        // Given
        val user = createUserHelper()
        every { authRepository.login("itshaider", "password") } returns Result.success(user)
        // When
        val result = useCase.invoke("itshaider", "password")
        // Then
        assertThat(result.isSuccess).isTrue()

    }

    @Test
    fun `Should return catch and rethrow exception result when login failed cause credentials`() {
        // Given
        every {
            authRepository.login(
                "itshaider",
                "password"
            )
        } returns Result.failure(InvalidCredentialsException("Invalid credentials"))
        // When & Then
        assertThrows<InvalidCredentialsException> { useCase.invoke("itshaider", "password") }
    }

    @Test
    fun `Should catch invalid password exception`() {
        // Given
        every {authRepository.login(password = "password", username = "itshaider")} returns Result.failure(InvalidPasswordException("Invalid Password"))
        // When & Then
        assertThrows< InvalidPasswordException> { useCase.invoke(password = "password", username = "itshaider") }
    }
    @Test
    fun `Should Save session if login success`() {
        // Given
        val user = createUserHelper()
        every { authRepository.login("itshaider", "password") } returns Result.success(user)
        // When
        useCase.invoke("itshaider", "password")
        // Then
        verify{sessionRepository.saveSession(any())}

    }
    @Test
    fun `Should return session entity when login success`() {
        // Given
        val user = createUserHelper()
        every { authRepository.login("itshaider", "password") } returns Result.success(user)
        // When
        val result = useCase.invoke("itshaider", "password")
        // Then
        assertThat(result.getOrNull()).isNotNull()
    }
}