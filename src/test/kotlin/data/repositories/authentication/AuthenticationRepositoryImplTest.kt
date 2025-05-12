package data.repositories.authentication

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.data.dto.user.UserDto
import org.baghdad.data.local.SessionDataSource
import org.baghdad.data.local.UserDataSource
import org.baghdad.data.mapper.toDomain
import org.baghdad.data.repositories.authentication.AuthenticationRepositoryImpl
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.InvalidPasswordException
import org.baghdad.logic.model.exceptions.LogoutFailedException
import org.baghdad.logic.utils.md5WithSalt
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID.randomUUID

class AuthenticationRepositoryImplTest {
    private lateinit var sessionDataSource: SessionDataSource
    private lateinit var userStorage: UserDataSource
    private lateinit var authRepository: AuthenticationRepositoryImpl

    val userName = "test"
    val password = "<PASSWORD>"

    @BeforeEach
    fun setUp() {
        userStorage = mockk()
        sessionDataSource = mockk(relaxed = true)
        authRepository = AuthenticationRepositoryImpl(userStorage, sessionDataSource)
    }

    @Test
    fun `should return UserEntity when credentials are valid`() = runTest {
        // Given
        val validUser = user.copy(hashedPassword = password.md5WithSalt())
        coEvery { userStorage.findUserByUsername(userName) } returns validUser

        val result = authRepository.login(userName, password.md5WithSalt())

        assertThat(result.id).isEqualTo(validUser.toDomain().id)
    }

    @Test
    fun `Should throw InvalidPasswordException  if credentials are valid  when login`() = runTest {
        // Given
        val inValidPasswordUser = user.copy(hashedPassword = "InvalidPassword")
        coEvery { userStorage.findUserByUsername(userName) } returns inValidPasswordUser
        // When & Then
        assertThrows<InvalidPasswordException> { authRepository.login(userName, "password") }

    }


    @Test
    fun `Should invoke delete session function  when logout`() = runTest {
        // Given
        coEvery { sessionDataSource.deleteSession() } returns true
        // When
        authRepository.logout()
        // Then
        coVerify { sessionDataSource.deleteSession() }
    }

    @Test
    fun `Should throw LogoutFailedException when logout fails`() = runTest {
        // Given
        coEvery { sessionDataSource.deleteSession() } returns false
        // When & Then
        assertThrows<LogoutFailedException> { authRepository.logout() }
    }
    val user = UserDto(
        username = "test",
        id = randomUUID(),
        name = "test",
        hashedPassword = "hashedPassword",
        type = UserType.Admin.toString()
    )
}