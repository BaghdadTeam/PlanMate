package logic.usecase.user

import helpers.authentication.createUserHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.model.exceptions.*
import org.baghdad.logic.repositories.UserRepository
import org.baghdad.logic.usecase.admin.AdminPermissionCheckerUseCase
import org.baghdad.logic.usecase.user.UserValidatorUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserValidatorUseCaseTest {


    private companion object {
        const val TEST_USERNAME = "aboud"
    }


    private lateinit var userRepository: UserRepository
    private lateinit var userValidatorUseCase: UserValidatorUseCase
    private lateinit var isAdmin: AdminPermissionCheckerUseCase


    @BeforeEach
    fun setup() {
        userRepository = mockk()
        isAdmin = AdminPermissionCheckerUseCase(userRepository)
        userValidatorUseCase = UserValidatorUseCase(userRepository)
    }

    @Test
    fun `should not throw any exception when validate user data`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        userValidatorUseCase.invoke(TEST_USERNAME,"hi123123" ,user.name)

    }

    @Test
    fun `should throw exception when username is blank`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        coEvery { userRepository.getUserById(user.id) } returns user

        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "",
                user.name,
                "h213123i",
            )
        }
    }

    @Test
    fun `should throw UserAlreadyExistsException when username is not unique`() = runTest {
        // Given
        val user = createUserHelper().copy(username = TEST_USERNAME)
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns true

        // When & Then
        assertThrows<UserAlreadyExistsException> {
            userValidatorUseCase.invoke(
                "aboud",
                "password",
                user.name,
            )
        }

    }

    @Test
    fun `should throw InvalidNameException when name is blank`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        // When & Then
        assertThrows<InvalidNameException> {
            userValidatorUseCase.invoke(
                "aboud",
                "password",
                "",
            )
        }

    }

    @Test
    fun `should throw InvalidPasswordException when password is blank`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        // When & Then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                "aboud",
                "",
                "aboud",
            )
        }
    }

    @Test
    fun `should throw InvalidPasswordException when password is too short`() = runTest {
        // Given
        val user = createUserHelper()
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false

        // when & then
        assertThrows<InvalidPasswordException> {
            userValidatorUseCase.invoke(
                "aboud",
                "12",
                "aboud",
            )
        }
    }

    @Test
    fun `should throw InvalidNameException when username is too short`() = runTest {
        // Given
        val user = createUserHelper().copy(name = "a")
        coEvery { userRepository.getUserById(user.id) } returns user
        coEvery { userRepository.isUsernameTaken(TEST_USERNAME) } returns false
        // When & Then
        assertThrows<InvalidUsernameException> {
            userValidatorUseCase.invoke(
                "a",
                "password",
                "aboud",
            )
        }
    }

}
