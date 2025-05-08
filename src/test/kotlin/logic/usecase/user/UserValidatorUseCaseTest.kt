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
        coEvery { userRepository.getUserByUsername("aboud") } throws UserNotFoundException("not found")
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        userValidatorUseCase.invoke("aboud", user.hashedPassword, user.name, user.id)

    }

    @Test
    fun `should throw exception when username is blank`()= runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserByUsername("aboud") } throws UserNotFoundException("not found")
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "",
                user.hashedPassword,
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
        coEvery { userRepository.getUserByUsername("aboud") } returns user

        // When & Then
        assertThrows<UserAlreadyExistsException> {
            userValidatorUseCase.invoke(
                "aboud",
                user.hashedPassword,
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
        coEvery { userRepository.getUserByUsername("aboud") } throws UserNotFoundException("not found")
        // When & Then
        assertThrows<UnauthorizedException> {
            userValidatorUseCase.invoke(
                "aboud",
                user.hashedPassword,
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
        coEvery { userRepository.getUserByUsername("aboud") } throws UserNotFoundException("not found")
        // When & Then
        assertThrows<InvalidNameException> {
            userValidatorUseCase.invoke(
                "aboud",
                user.hashedPassword,
                "",
                user.id
            )
        }

    }

    @Test
    fun `should throw InvalidPasswordException when password is blank`()= runTest {
        // Given
        val user = createUserHelper().copy(hashedPassword = "")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.getUserByUsername("aboud") } throws UserNotFoundException("not found")
        // When & Then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                "aboud",
                user.hashedPassword,
                "aboud",
                user.id
            )
        }
    }

    @Test
    fun `should throw InvalidPasswordException when password is too short`()= runTest {
        // Given
        val user = createUserHelper().copy(hashedPassword = "12")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.getUserByUsername("aboud") } throws UserNotFoundException("not found")

        // when & then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                "aboud",
                user.hashedPassword,
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
        coEvery { userRepository.getUserByUsername("aboud") } throws UserNotFoundException("not found")
        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "a",
                user.hashedPassword,
                "aboud",
                user.id
            )
        }
    }

}