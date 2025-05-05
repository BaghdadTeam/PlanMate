package data.repositories.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.baghdad.data.local.UserDataSource
import org.baghdad.data.repositories.authentication.AuthenticationRepositoryImpl
import org.baghdad.logic.model.exceptions.InvalidCredentialsException
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.logic.utils.md5WithSalt
import org.junit.jupiter.api.*

class AuthenticationRepositoryImplTest {
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userStorage: UserDataSource
    private lateinit var authRepository: AuthenticationRepositoryImpl

    val userName = "test"
    val password = "<PASSWORD>"

    @BeforeEach
    fun setUp() {
        userStorage = mockk()
        sessionRepository = mockk(relaxed = true)
        authRepository = AuthenticationRepositoryImpl(userStorage, sessionRepository)
    }

    @Test
    fun ` should  return user entity if credentials are valid  when login`() {
        // Given
        val user = createUserHelper(userName, password.md5WithSalt())
        every { userStorage.findUserByUsername(userName) } returns user
        // When & Then
        assertThat(authRepository.login(userName, password.md5WithSalt()).id).isEqualTo(user.id)
    }

    @Test
    fun `Should throw InvalidPasswordException  if credentials are valid  when login`() {
        // Given
        val user = createUserHelper(userName, "invalidPassword".md5WithSalt())
        every { userStorage.findUserByUsername(userName) } returns user
        // When & Then
        assertThrows<InvalidPasswordException>{authRepository.login(userName, password)}

    }


    @Test
    fun `Should invoke delete session function  when logout`() {
        // Given
        every { sessionRepository.deleteSession() } returns true
        // When
        authRepository.logout()
        // Then
        verify { sessionRepository.deleteSession() }
    }

    @Test
    fun `Should throw LogoutFailedException when logout fails`() {
        // Given
        every { sessionRepository.deleteSession() } returns false
        // When & Then
        assertThrows<LogoutFailedException> { authRepository.logout() }
    }

}