package logic.usecase.user

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.manager.SessionManager
import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUserByUsernameUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var getUserByUsernameUseCase: GetUserByUsernameUseCase
    private val sessionManager: SessionManager = mockk(relaxed = true)

    private val sampleUser = createSampleUser()

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        getUserByUsernameUseCase = GetUserByUsernameUseCase(userRepository,sessionManager)
        coEvery { sessionManager.isAuthenticated() } returns true


    }

    @Test
    fun `should return user when user exists`() = runTest {
        // Given
        coEvery { userRepository.getUserByUsername("alice") } returns sampleUser

        // When
        val result = getUserByUsernameUseCase("alice")

        // Then
        assertEquals(sampleUser, result)
    }

    @Test
    fun `should throw InvalidUsernameException when username is blank`() = runTest {
        // When & Then
        assertFailsWith<InvalidUsernameException> {
            getUserByUsernameUseCase("")
        }
    }

    @Test
    fun `should throw UserNotFoundException when user does not exist`() = runTest {
        // Given
        coEvery { userRepository.getUserByUsername("bob") } throws UserNotFoundException("User 'bob' not found.")

        // When & Then
        assertFailsWith<UserNotFoundException> {
            getUserByUsernameUseCase("bob")
        }
    }

    private fun createSampleUser(): UserEntity {
        return UserEntity(
            name = "Alice",
            username = "alice",
            type = UserType.Mate
        )
    }
}
