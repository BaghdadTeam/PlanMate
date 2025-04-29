package logic.repositories.authentication

import com.google.common.truth.Truth.assertThat
import helpers.authentication.createUserHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import org.baghdad.data.storage.user.UserStorage
import org.baghdad.logic.entities.authentication.SessionEntity
import org.baghdad.logic.repositories.authentication.AuthenticationRepositoryImpl
import org.baghdad.logic.repositories.authentication.SessionRepository
import org.baghdad.utils.passwordutils.md5WithSalt
import org.junit.jupiter.api.*
import java.util.UUID

class AuthenticationRepositoryImplTest {
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userStorage: UserStorage
    private lateinit var authRepository: AuthenticationRepositoryImpl

    val userName = "test"
    val password = "<PASSWORD>"
    @BeforeEach
    fun setUp(){
        userStorage = mockk()
        sessionRepository = mockk()
        authRepository = AuthenticationRepositoryImpl(sessionRepository, userStorage)
    }
    @Test
    fun ` should  return a success result if credentials are valid  when login`() {
        // Given
        val user = createUserHelper(userName, password.md5WithSalt())
        every { userStorage.findUserByUsername(userName) } returns user
        // When & Then
        assertThat(authRepository.login(userName, password).isSuccess).isTrue()
    }
    @Test
    fun `Should return failure result password is invalid when login`() {
        // Given
        val user = createUserHelper(userName, "invalidPassword".md5WithSalt())
        every { userStorage.findUserByUsername(userName) } returns user
        // When & Then
        assertThat(authRepository.login(userName,password).isFailure).isTrue()

    }
    @Test
    fun `Should return failure result user does not exist when login`() {
        // Given  
        every { userStorage.findUserByUsername(any()) } returns null
        // When & Then 
        assertThat(authRepository.login("test","test").isFailure).isTrue()
    }
    @Test
    fun `Should invoke save session function if login success when login`() {
        // When
        val user = createUserHelper(userName, password.md5WithSalt())
        every { userStorage.findUserByUsername(userName) } returns user
        // When
        authRepository.login(userName, password.md5WithSalt())
        // Then
        verify {
            sessionRepository.saveSession(SessionEntity
                (userData = user, token = UUID.randomUUID().toString(), loginTime = LocalDateTime.now())
            )
        }
    }
    @Test
    fun `Should invoke delete session function  when logout`() {
        authRepository.logout()
        verify {sessionRepository.deleteSession()}
    }
    @Test
    fun `Should return failure result when logout fails`() {
        // Given
        every { sessionRepository.deleteSession() } throws RuntimeException("Failed to delete session")
        // When
        val result = authRepository.logout()
        // Then
        assertThat(result.isFailure).isTrue()
    }
    @Test
    fun `Should return success result when logout success`() {
        every { sessionRepository.deleteSession() } returns true
        val result = authRepository.logout()
        assertThat(result.isSuccess).isTrue()
    }
}