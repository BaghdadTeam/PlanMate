package logic.usecase.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.repositories.AuthenticationRepository
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.repositories.TokenProvider
import org.baghdad.logic.usecase.authentication.LoginUseCase
import org.baghdad.utils.md5WithSalt
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase
    private lateinit var authRepository: AuthenticationRepository
    private lateinit var sessionRepository: SessionRepository
    private lateinit var tokenProvider: TokenProvider

    @BeforeEach
    fun setUp() {
        authRepository = mockk(relaxed = true)
        sessionRepository = mockk(relaxed = true)
        tokenProvider = mockk(relaxed = true)

        useCase = LoginUseCase(authRepository, sessionRepository, tokenProvider)
    }

    @Test
    fun `should return session entity and save session when login is successful`() = runTest {
        // Given
        val user = createUserHelper()
        val hashed = "password".md5WithSalt()

        coEvery { authRepository.login("itshaider", hashed) } returns user

        // When
        val result = useCase.invoke("itshaider", "password")

        // Then
        assertThat(result).isNotNull()
        coVerify { sessionRepository.saveSession(any()) }
    }

    @Test
    fun `should throws InvalidCredentialsException when login fails due to invalid credentials`() = runTest {
        // Given
        val hashed = "password".md5WithSalt()
        coEvery {
            authRepository.login(
                "itshaider",
                hashed
            )
        } throws InvalidCredentialsException("Invalid hashed")
        // When & Then
        assertThrows<InvalidCredentialsException> { useCase.invoke("itshaider", "password") }
    }


    @Test
    fun `should throws InvalidPasswordException due to invalid password`() = runTest {
        // Given
        val hashed = "password".md5WithSalt()
        coEvery { authRepository.login("itshaider", hashed) } throws InvalidPasswordException("Invalid password")
        // When & Then
        assertThrows<InvalidPasswordException> { useCase.invoke("itshaider", "password") }

    }

    @Test
    fun `should call saveSession when login is successful`() = runTest {
        // Given
        val user = createUserHelper()
        val hashed = "password".md5WithSalt()
        coEvery { authRepository.login("itshaider", hashed) } returns user

        // When
        useCase.invoke("itshaider", "password")

        // Then
        coVerify { sessionRepository.saveSession(any()) }
    }

    @Test
    fun `should throws InvalidCredentialsException when login fails due blank username `() = runTest {
        val hashed = "password".md5WithSalt()
        val username = "itshaider"
        coEvery { authRepository.login(username, hashed) } returns createUserHelper()
        // When & Then
        assertThrows<InvalidCredentialsException> { useCase.invoke("", "") }
    }

    @Test
    fun `should throws InvalidCredentialsException when login fails due blank password `() = runTest {
        val hashed = "password".md5WithSalt()
        val username = "itshaider"
        coEvery { authRepository.login(username, hashed) } returns createUserHelper()
        // When & Then
        assertThrows<InvalidCredentialsException> { useCase.invoke("itshaider", "") }
    }
}