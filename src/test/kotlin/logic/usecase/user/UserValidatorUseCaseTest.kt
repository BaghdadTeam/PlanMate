package logic.usecase.user

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.user.InvalidNameException
import org.baghdad.logic.model.exceptions.user.InvalidPasswordException
import org.baghdad.logic.model.exceptions.user.InvalidUsernameException
import org.baghdad.logic.model.exceptions.user.UnauthorizedException
import org.baghdad.logic.model.exceptions.user.UserAlreadyExistsException
import org.baghdad.logic.model.exceptions.user.UserNotFoundException
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.user.UserValidatorUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserValidatorUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userValidatorUseCase: UserValidatorUseCase

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        userValidatorUseCase = UserValidatorUseCase(userRepository)
    }

    @Test
    fun `should not throw any exception when validate user data`()= runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.isUsernameTaken("aboud") } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        userValidatorUseCase.invoke("aboud", "password", user.name, user.id)

    }

    @Test
    fun `should throw exception when username is blank`()= runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.isUsernameTaken("aboud") } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "",
                "password",
                user.name,
                user.id
            )
        }
    }

    @Test
    fun `should throw UserAlreadyExistsException when username is not unique`()= runTest {
        // Given
        val user = createUserHelper().copy(username = "aboud")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken("aboud") } returns true

        // When & Then
        assertThrows<UserAlreadyExistsException> {
            userValidatorUseCase.invoke(
                "aboud",
                "password",
                user.name,
                user.id
            )
        }

    }

    @Test
    fun `should throw UnauthorizedException when user is not admin`()= runTest {
        // Given
        val user = createUserHelper().copy(type = UserType.Mate)
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken("aboud") } returns false
        // When & Then
        assertThrows<UnauthorizedException> {
            userValidatorUseCase.invoke(
                "aboud",
                "password",
                user.name,
                user.id
            )
        }
    }

    @Test
    fun `should throw InvalidNameException when name is blank`()= runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken("aboud") } returns false
        // When & Then
        assertThrows<InvalidNameException> {
            userValidatorUseCase.invoke(
                "aboud",
                "password",
                "",
                user.id
            )
        }

    }

    @Test
    fun `should throw InvalidPasswordException when password is blank`()= runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken("aboud") } returns false
        // When & Then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                "aboud",
                "",
                "aboud",
                user.id
            )
        }
    }

    @Test
    fun `should throw InvalidPasswordException when password is too short`()= runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken("aboud") } returns false

        // when & then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                "aboud",
                "12",
                "aboud",
                user.id
            )
        }
    }

    @Test
    fun `should throw InvalidNameException when username is too short`()= runTest {
        // Given
        val user = createUserHelper().copy(name = "a")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken("aboud") } returns false
        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "a",
                "password",
                "aboud",
                user.id
            )
        }
    }

}