package data.repositories.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.local.UserDataSource
import org.baghdad.data.repositories.authentication.AuthenticationRepositoryImpl
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.repositories.SessionRepository
import org.baghdad.utils.md5WithSalt
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    fun ` should  return user entity if credentials are valid  when login`() = runTest {
        // Given
        val user = createUserHelper(userName, password.md5WithSalt())
        coEvery { userStorage.findUserByUsername(userName) } returns user
        // When & Then
        assertThat(authRepository.login(userName, password.md5WithSalt()).id).isEqualTo(user.id)
    }

    @Test
    fun `Should throw InvalidPasswordException  if credentials are valid  when login`() = runTest {
        // Given
        val user = createUserHelper(userName, "invalidPassword".md5WithSalt())
        coEvery { userStorage.findUserByUsername(userName) } returns user
        // When & Then
        assertThrows<InvalidPasswordException> { authRepository.login(userName, password) }

    }


    @Test
    fun `Should invoke delete session function  when logout`() = runTest {
        // Given
        coEvery { sessionRepository.deleteSession() } returns true
        // When
        authRepository.logout()
        // Then
        coVerify { sessionRepository.deleteSession() }
    }

    @Test
    fun `Should throw LogoutFailedException when logout fails`() = runTest {
        // Given
        coEvery { sessionRepository.deleteSession() } returns false
        // When & Then
        assertThrows<LogoutFailedException> { authRepository.logout() }
    }

}